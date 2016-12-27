package net.digitalid.database.jdbc;

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

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedFailureException;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.operation.FailedCommitException;
import net.digitalid.database.exceptions.operation.FailedConnectionException;
import net.digitalid.database.exceptions.operation.FailedKeyGenerationException;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedOperationException;
import net.digitalid.database.exceptions.operation.FailedQueryExecutionException;
import net.digitalid.database.exceptions.operation.FailedStatementCreationException;
import net.digitalid.database.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.interfaces.DatabaseInstance;
import net.digitalid.database.interfaces.SQLDecoder;

/**
 * This classes uses the JDBC connection to execute the statements.
 */
@Mutable
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
    @Impure
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
    @Impure
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
    @Impure
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
    @Impure
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
    @Impure
    @NonCommitting
    protected void begin() throws FailedConnectionException {
        checkConnection();
        transaction.set(Boolean.TRUE);
    }
    
    @Impure
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
    
    @Impure
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
     * @param statement the statement which is to be prepared.
     * @param generatesKeys whether the statement generates keys.
     *                      TODO: instead of allowing to set generatesKeys here, we need to adjust the insert statements where generated keys are expected to query with statement.getGeneratedKeys().
     * 
     * @return the prepared statement that is ready for execution.
     */
    @Pure
    protected @Nonnull PreparedStatement prepare(@Nonnull String statement, boolean generatesKeys) throws FailedNonCommittingOperationException, InternalException {
        try {
            final @Nonnull PreparedStatement preparedStatement = getConnection().prepareStatement(statement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return preparedStatement;
        } catch (@Nonnull SQLException exception) {
            throw FailedStatementCreationException.get(exception);
        }
    }
    
    // TODO: move transcription to database conversion.
    /**
     * Prepares the given statement at the given site.
     * 
     * @param site the site at which the statement is prepared.
     * @param statement the statement which is to be prepared.
     * @param generatesKeys whether the statement generates keys.
     * 
     * @return the prepared statement that is ready for execution.
     */
/*    protected @Nonnull PreparedStatement prepare(@Nonnull Site site, @Nonnull SQLNode statement, boolean generatesKeys) throws FailedNonCommittingOperationException, InternalException {
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
    }*/
    
    /**
     * Executes the given statement at the given site.
     * 
     * @param statement the statement which is to be executed.
     * 
     * @return the number of rows affected by the given statement.
     */
    @Impure
    protected int executeUpdate(@Nonnull String statement) throws FailedNonCommittingOperationException, InternalException {
        try {
            return getConnection().createStatement().executeUpdate(statement);
            //prepare(statement, false, tables).executeUpdate();
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
    @Impure
    protected int executeUpdate(@Nonnull JDBCEncoder encoder) throws FailedNonCommittingOperationException, InternalException {
        try {
            int result = 0;
            for (@Nonnull PreparedStatement preparedStatement : encoder.getPreparedStatements()) {
                result += preparedStatement.executeUpdate();
            }
            return result;
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
    @Impure
    protected int executeBatch(@Nonnull JDBCEncoder encoder) throws FailedNonCommittingOperationException, InternalException {
        try {
            int result = 0;
            for (@Nonnull PreparedStatement preparedStatement : encoder.getPreparedStatements()) {
                int[] updatedRows = preparedStatement.executeBatch();
                for (int updatedRowCount : updatedRows) {
                    result += updatedRowCount;
                }
                commit();
            }
            return result;
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        } catch (FailedCommitException exception) {
            // TODO: exception
            throw UnexpectedFailureException.with(exception);
        }
    }
    
    // TODO: Move to conversion package.
/*    @Override
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
    }*/
    
    @Impure
    @Override
    public @Nonnull SQLDecoder executeSelect(@Nonnull String selectStatement) throws FailedNonCommittingOperationException, InternalException {
        try {
            final @Nonnull ResultSet resultSet = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(selectStatement);
            return JDBCDecoder.get(resultSet);
        } catch (@Nonnull SQLException exception) {
            throw FailedQueryExecutionException.get(exception);
        }
    }
     
    @Impure
    public @Nonnull SQLDecoder executeSelect(@Nonnull JDBCEncoder encoder) throws FailedNonCommittingOperationException, InternalException {
        // TODO: Implement wrapper around ResultSet such that multiple result-sets can be retrieved. Alternatively, make sure that only one prepared statement exists.
        try {
            final @Nonnull PreparedStatement preparedStatement = encoder.getPreparedStatements().getFirst();
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
            return JDBCDecoder.get(resultSet);
        } catch (@Nonnull SQLException exception) {
            throw FailedQueryExecutionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public long executeAndReturnGeneratedKey(@Nonnull String insertStatement) throws FailedNonCommittingOperationException, InternalException {
        try {
            final @Nonnull Statement statement = getConnection().createStatement();
            statement.executeUpdate(insertStatement);
            try (@Nonnull ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) { return resultSet.getLong(1); }
                else { throw new SQLException("The prepared statement did not generate a key."); }
            } catch (@Nonnull SQLException exception) {
                throw FailedKeyGenerationException.get(exception);
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
}
