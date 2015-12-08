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
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.Committing;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.FailedCommitException;
import net.digitalid.database.core.exceptions.operation.FailedConnectionException;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedKeyGenerationException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedNonCommittingOperationException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedPreparedStatementCreationException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedQueryExecutionException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedStatementCreationException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedUpdateExecutionException;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.tuples.FreezablePair;
import net.digitalid.utility.collections.tuples.ReadOnlyPair;
import net.digitalid.utility.system.errors.ShouldNeverHappenError;
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
     * Returns a new connection to the database.
     * 
     * @return a new connection to the database.
     */
    @Pure
    private @Nonnull Connection getNewConnection() throws FailedConnectionException {
        try {
            final @Nonnull Connection connection = DriverManager.getConnection(getURL(), getProperties());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            return connection;
        } catch (@Nonnull SQLException exception) {
            throw FailedConnectionException.get(exception);
        }
    }
    
    /**
     * Stores the open connection to the database that is associated with the current thread or the exception that was thrown instead.
     */
    private final @Nonnull ThreadLocal<ReadOnlyPair<Connection, FailedConnectionException>> connection = new ThreadLocal<ReadOnlyPair<Connection, FailedConnectionException>>() {
        @Override protected @Nonnull ReadOnlyPair<Connection, FailedConnectionException> initialValue() { // TODO: Replace the ReadOnlyPair with a simple Pair.
            try {
                return FreezablePair.get(getNewConnection(), (FailedConnectionException) null).freeze();
            } catch (@Nonnull FailedConnectionException exception) {
                return FreezablePair.get((Connection) null, exception).freeze();
            }
        }
    };
    
    /**
     * Checks that the connection to the database is still valid.
     * 
     * @param recurse whether the check is to be repeated or not.
     */
    @Pure
    @NonCommitting
    public void checkConnection(boolean recurse) throws FailedConnectionException {
        final @Nonnull ReadOnlyPair<Connection, FailedConnectionException> pair = connection.get();
        
        if (pair.getNullableElement0() == null) {
            connection.remove();
            throw pair.getNonNullableElement1();
        }
        
        try {
            if (!pair.getNonNullableElement0().isValid(1)) {
                Log.information("The database connection is no longer valid and is thus replaced.");
                connection.remove();
                if (recurse) { checkConnection(false); }
                else { throw new SQLException("The database connection remains invalid."); }
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedConnectionException.get(exception);
        }
    }
    
    /**
     * Returns the open connection to the database that is associated with the current thread.
     * <p>
     * <em>Important:</em> Do not commit or close the connection as it will be reused later on!
     * 
     * @return the open connection to the database that is associated with the current thread.
     */
    @Pure
    @NonCommitting
    protected @Nonnull Connection getConnection() {
        final @Nonnull ReadOnlyPair<Connection, FailedConnectionException> pair = connection.get();
        if (pair.getNullableElement0() != null) {
            return pair.getNonNullableElement0();
        } else {
            connection.remove();
            throw ShouldNeverHappenError.get("The connection should have been checked by the locking method.");
        }
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    @Override
    @Committing
    public void commit() throws FailedCommitException {
        try {
            getConnection().commit();
        } catch (@Nonnull SQLException exception) {
            throw FailedCommitException.get(exception);
        }
    }
    
    @Override
    @Committing
    public void rollback() {
        try {
            getConnection().rollback();
        } catch (@Nonnull SQLException exception) {
            Log.error("Could not roll back the transaction.", exception);
        }
    }
    
    /* -------------------------------------------------- Statements -------------------------------------------------- */
    
    /**
     * Creates a new statement on the connection of the current thread.
     * 
     * @return a new statement on the connection of the current thread.
     */
    @NonCommitting
    public @Nonnull Statement createStatement() throws FailedStatementCreationException {
        try {
            return getConnection().createStatement();
        } catch (@Nonnull SQLException exception) {
            throw FailedStatementCreationException.get(exception);
        }
    }
    
    /**
     * Prepares the statement on the connection of the current thread.
     * 
     * @param SQL the statement which is to be prepared for later use.
     * 
     * @return a new statement on the connection of the current thread.
     */
    @NonCommitting
    public @Nonnull PreparedStatement prepareStatement(@Nonnull String SQL) throws FailedPreparedStatementCreationException {
        try {
            return getConnection().prepareStatement(SQL);
        } catch (@Nonnull SQLException exception) {
            throw FailedPreparedStatementCreationException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Insertions -------------------------------------------------- */
    
    /**
     * Executes the given insertion and returns the generated key.
     * 
     * @param statement a statement to execute the insertion.
     * @param SQL an SQL statement that inserts an entry.
     * 
     * @return the key generated for the inserted entry.
     */
    @NonCommitting
    protected long executeInsert(@Nonnull Statement statement, @Nonnull String SQL) throws FailedKeyGenerationException, FailedUpdateExecutionException {
        try {
            statement.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
        
        try (@Nonnull ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) { return resultSet.getLong(1); }
            else { throw new SQLException("The given SQL statement did not generate a key."); }
        } catch (@Nonnull SQLException exception) {
            throw FailedKeyGenerationException.get(exception);
        }
    }
    
    /**
     * Returns a prepared statement that can be used to insert values and retrieve their key.
     * 
     * @param SQL the insert statement which is to be prepared for returning the generated keys.
     * 
     * @return a prepared statement that can be used to insert values and retrieve their key.
     */
    @NonCommitting
    public @Nonnull PreparedStatement prepareInsertStatement(@Nonnull String SQL) throws FailedPreparedStatementCreationException {
        try {
            return getConnection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        } catch (@Nonnull SQLException exception) {
            throw FailedPreparedStatementCreationException.get(exception);
        }
    }
    
    /**
     * Returns the key generated by the given prepared statement.
     * 
     * @param preparedStatement an executed prepared statement that has generated a key.
     * 
     * @return the key generated by the given prepared statement.
     */
    @NonCommitting
    public long getGeneratedKey(@Nonnull PreparedStatement preparedStatement) throws FailedKeyGenerationException {
        try (@Nonnull ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (resultSet.next()) { return resultSet.getLong(1); }
            else { throw new SQLException("The given SQL statement did not generate a key."); }
        } catch (@Nonnull SQLException exception) {
            throw FailedKeyGenerationException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    /**
     * Executes the given statement at the given site.
     */
    @Override
    public @Nonnull SelectionResult execute(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws FailedNonCommittingOperationException, InternalException {
        final @Nonnull StringBuilder string = new StringBuilder();
        selectStatement.transcribe(Database.getDialect(), site, string);
        final @Nonnull PreparedStatement preparedStatement = prepareStatement(string.toString());
        final @Nonnull ValueCollector collector = JDBCValueCollector.get(preparedStatement);
        selectStatement.storeValues(collector);
        try {
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
            return JDBCSelectionResult.get(resultSet);
        } catch (@Nonnull SQLException exception) {
            throw FailedQueryExecutionException.get(exception);
        }
    }
    
}
