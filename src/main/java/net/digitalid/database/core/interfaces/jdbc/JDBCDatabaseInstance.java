package net.digitalid.database.core.interfaces.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.Committing;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.FailedCommitException;
import net.digitalid.database.core.exceptions.operation.FailedConnectionException;
import net.digitalid.database.core.exceptions.operation.FailedKeyGenerationException;
import net.digitalid.database.core.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.FailedQueryExecutionException;
import net.digitalid.database.core.exceptions.operation.FailedStatementCreationException;
import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.statement.delete.SQLDeleteStatement;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.core.sql.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.core.sql.statement.update.SQLUpdateStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;
import net.digitalid.utility.system.logger.Log;

/**
 * This classes uses the JDBC connection to execute the statements.
 */
@Immutable
public abstract class JDBCDatabaseInstance implements DatabaseInstance {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new database instance with the given driver.
     * 
     * @param driver the JDBC driver of this database instance.
     */
    protected JDBCDatabaseInstance(@Nonnull Driver driver) {}
    
    /* -------------------------------------------------- Binary Stream -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean supportsBinaryStreams() {
        return true;
    }
    
    /* -------------------------------------------------- Database -------------------------------------------------- */
    
    /**
     * Returns the URL of this database instance.
     * 
     * @return the URL of this database instance.
     */
    @Pure
    protected abstract @Nonnull String getURL();
    
    /**
     * Returns the properties of this instance.
     * <p>
     * <em>Important:</em> Do not modify them!
     * 
     * @return the properties of this instance.
     */
    @Pure
    protected abstract @Nonnull Properties getProperties();
    
    /**
     * Drops this database instance.
     */
    @Committing
    public abstract void dropDatabase() throws FailedOperationException;
    
    /* -------------------------------------------------- Connection -------------------------------------------------- */
    
    /**
     * Stores the database connection of the current thread.
     */
    private final @Nonnull ThreadLocal<Connection> connection = new ThreadLocal<>();
    
    /**
     * Sets a new database connection for the current thread.
     */
    @NonCommitting
    private void setConnection() throws FailedConnectionException {
        try {
            final @Nonnull Connection connection = DriverManager.getConnection(getURL(), getProperties());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            this.connection.set(connection);
        } catch (@Nonnull SQLException exception) {
            throw FailedConnectionException.get(exception);
        }
    }
    
    /**
     * Checks that the database connection of the current thread is valid.
     */
    @NonCommitting
    private void checkConnection() throws FailedConnectionException {
        final @Nullable Connection connection = this.connection.get();
        if (connection == null) {
            setConnection();
        } else {
            try {
                if (!connection.isValid(1)) {
                    Log.information("The database connection is no longer valid and is thus replaced.");
                    connection.close();
                    setConnection();
                }
            } catch (@Nonnull SQLException exception) {
                throw FailedConnectionException.get(exception);
            }
        }
    }
    
    /**
     * Returns the database connection of the current thread.
     * <p>
     * <em>Important:</em> Do not commit, roll back or close
     * the current connection as it will be reused later on!
     * 
     * @return the database connection of the current thread.
     */
    @NonCommitting
    protected final @Nonnull Connection getConnection() throws FailedConnectionException {
        if (!transaction.get()) { begin(); }
        return connection.get();
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Stores whether the database connection of the current thread is in a transaction.
     */
    private final @Nonnull ThreadLocal<Boolean> transaction = new ThreadLocal<Boolean>() {
        @Override protected @Nonnull Boolean initialValue() { return Boolean.FALSE; }
    };
    
    /**
     * Begins a new transaction.
     */
    @NonCommitting
    protected void begin() throws FailedConnectionException {
        checkConnection();
        transaction.set(Boolean.TRUE);
    }
    
    @Override
    @Committing
    public void commit() throws FailedCommitException {
        try {
            getConnection().commit();
            transaction.set(Boolean.FALSE);
        } catch (@Nonnull FailedConnectionException exception) {
            throw FailedCommitException.get(new SQLException("This should never happen because the connection is checked when the transaction is started.", exception));
        } catch (@Nonnull SQLException exception) {
            throw FailedCommitException.get(exception);
        }
    }
    
    @Override
    @Committing
    public void rollback() {
        try {
            getConnection().rollback();
            transaction.set(Boolean.FALSE);
        } catch (@Nonnull FailedConnectionException | SQLException exception) {
            Log.error("Could not roll back the transaction.", exception);
        }
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    /**
     * Prepares the given statement at the given site.
     * 
     * @param site the site at which the statement is prepared.
     * @param statement the statement which is to be prepared.
     * @param generatesKeys whether the statement generates keys.
     * 
     * @return the prepared statement that is ready for execution.
     */
    protected @Nonnull PreparedStatement prepare(@Nonnull Site site, @Nonnull SQLNode statement, boolean generatesKeys) throws FailedNonCommittingOperationException, InternalException {
        final @Nonnull StringBuilder string = new StringBuilder();
        statement.transcribe(Database.getDialect(), site, string);
        try {
            final @Nonnull PreparedStatement preparedStatement = getConnection().prepareStatement(string.toString(), generatesKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
            if (statement instanceof SQLParameterizableNode) {
                final @Nonnull ValueCollector collector = JDBCValueCollector.get(preparedStatement);
                ((SQLParameterizableNode) statement).storeValues(collector);
            }
            return preparedStatement;
        } catch (@Nonnull SQLException exception) {
            throw FailedStatementCreationException.get(exception);
        }
    }
    
    /**
     * Executes the given statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param statement the statement which is to be executed.
     * 
     * @return the number of rows affected by the given statement.
     */
    protected int executeUpdate(@Nonnull Site site, @Nonnull SQLNode statement) throws FailedNonCommittingOperationException, InternalException {
        try {
            return prepare(site, statement, false).executeUpdate();
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
    @Override
    public void execute(@Nonnull Site site, @Nonnull SQLCreateTableStatement createTableStatement) throws FailedNonCommittingOperationException, InternalException {
        executeUpdate(site, createTableStatement);
    }
    
    @Override
    public void execute(@Nonnull Site site, @Nonnull SQLDropTableStatement dropTableStatement) throws FailedNonCommittingOperationException, InternalException {
        executeUpdate(site, dropTableStatement);
    }
    
    @Override
    public void execute(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException {
        executeUpdate(site, insertStatement);
    }
    
    @Override
    public int execute(@Nonnull Site site, @Nonnull SQLUpdateStatement updateStatement) throws FailedNonCommittingOperationException, InternalException {
        return executeUpdate(site, updateStatement);
    }
    
    @Override
    public int execute(@Nonnull Site site, @Nonnull SQLDeleteStatement deleteStatement) throws FailedNonCommittingOperationException, InternalException {
        return executeUpdate(site, deleteStatement);
    }
    
    @Override
    public @Nonnull SelectionResult execute(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws FailedNonCommittingOperationException, InternalException {
        final @Nonnull PreparedStatement preparedStatement = prepare(site, selectStatement, false);
        try {
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
            return JDBCSelectionResult.get(resultSet);
        } catch (@Nonnull SQLException exception) {
            throw FailedQueryExecutionException.get(exception);
        }
    }
    
    @Override
    public long executeAndReturnGeneratedKey(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException {
        final @Nonnull PreparedStatement preparedStatement = prepare(site, insertStatement, true);
        try {
            preparedStatement.executeUpdate();
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
        try (@Nonnull ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (resultSet.next()) { return resultSet.getLong(1); }
            else { throw new SQLException("The prepared statement did not generate a key."); }
        } catch (@Nonnull SQLException exception) {
            throw FailedKeyGenerationException.get(exception);
        }
    }
    
}
