package net.digitalid.database.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.sql.SQLStatement;
import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.jdbc.encoder.JDBCActionEncoderBuilder;
import net.digitalid.database.jdbc.encoder.JDBCQueryEncoderBuilder;
import net.digitalid.database.unit.Unit;

/**
 * This classes uses the JDBC connection to execute the statements.
 */
@Mutable
public abstract class JDBCDatabase extends Database {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new database instance with the given driver.
     * 
     * @param driver the JDBC driver of this database instance.
     */
    protected JDBCDatabase(@Nonnull Driver driver) {}
    
    /* -------------------------------------------------- Database -------------------------------------------------- */
    
    /**
     * Returns the URL of this database instance.
     */
    @Pure
    protected abstract @Nonnull String getURL();

    /**
     * Returns the properties of this instance.
     * <p>
     * <em>Important:</em> Do not modify them!
     */
    @Pure
    protected abstract @Nonnull Properties getProperties();
    
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
    private void setConnection() throws DatabaseException {
        try {
            final @Nonnull Connection connection = DriverManager.getConnection(getURL(), getProperties());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            this.connection.set(connection);
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    /**
     * Checks that the database connection of the current thread is valid.
     */
    @Impure
    @NonCommitting
    private void checkConnection() throws DatabaseException {
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
                throw DatabaseExceptionBuilder.withCause(exception).build();
            }
        }
    }
    
    /**
     * Returns the database connection of the current thread.
     * <p>
     * <em>Important:</em> Do not commit, roll back or close
     * the current connection as it will be reused later on!
     */
    @Impure
    @NonCommitting
    protected @Nonnull Connection getConnection() throws DatabaseException {
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
    protected void begin() throws DatabaseException {
        checkConnection();
        transaction.set(Boolean.TRUE);
    }
    
    @Impure
    @Override
    @Committing
    public void commit() throws DatabaseException {
        try {
            getConnection().commit();
            transaction.set(Boolean.FALSE);
            runRunnablesAfterCommit();
        } catch (@Nonnull SQLException exception) {
            runRunnablesAfterRollback();
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    @Committing
    public void rollback() {
        try {
            getConnection().rollback();
            transaction.set(Boolean.FALSE);
        } catch (@Nonnull SQLException | DatabaseException exception) {
            Log.error("Could not roll back the transaction.", exception);
        } finally {
            runRunnablesAfterRollback();
        }
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    /**
     * Prepares the given statement at the given site.
     * 
     * @param statement the statement which is to be prepared.
     * 
     * @return the prepared statement that is ready for execution.
     */
    @Pure
    protected @Nonnull PreparedStatement prepare(@Nonnull String statement) throws DatabaseException {
        try {
            final @Nonnull PreparedStatement preparedStatement = getConnection().prepareStatement(statement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return preparedStatement;
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void close() throws Exception {
        getConnection().close();
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
    
    @PureWithSideEffects
    private void executeStatement(@Nonnull Unit unit, @Nonnull SQLTableStatement tableStatement) throws DatabaseException {
        final @Nonnull String statementAsString = SQLDialect.unparse(tableStatement, unit);
        Log.debugging("Executing $", statementAsString);
        try {
            // TODO: do we really need to set the result set type and result set concurrency?
            getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).execute(statementAsString);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @PureWithSideEffects
    protected void execute(@Nonnull SQLTableStatement tableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(unit, tableStatement);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(unit, createTableStatement);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLDropTableStatement dropTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(unit, dropTableStatement);
    }
    
    /* -------------------------------------------------- Encoder -------------------------------------------------- */
    
    @PureWithSideEffects
    private @Nonnull SQLActionEncoder getEncoderForStatement(@Nonnull SQLTableStatement tableStatement, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull String statementAsString = SQLDialect.unparse(tableStatement, unit);
        Log.debugging("Executing $", statementAsString);
        // FIXME: The converter generator does not recognize that the sql encoder implementation already implements the methods getRepresentation(), isHashing(), isCompressing() and isEncryption().
        return JDBCActionEncoderBuilder.withPreparedStatement(prepare(statementAsString)).withRepresentation(Representation.INTERNAL).withHashing(false).withCompressing(false).withEncrypting(false).build();
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLInsertStatement insertStatement, @Nonnull Unit unit) throws DatabaseException {
        return getEncoderForStatement(insertStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLUpdateStatement updateStatement, @Nonnull Unit unit) throws DatabaseException {
        return getEncoderForStatement(updateStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLDeleteStatement deleteStatement, @Nonnull Unit unit) throws DatabaseException {
        return getEncoderForStatement(deleteStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull SQLSelectStatement selectStatement, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull StringBuilder sqlStringBuilder = new StringBuilder();
        selectStatement.unparse(SQLDialect.instance.get(), unit, sqlStringBuilder);
        Log.debugging("Executing $", sqlStringBuilder.toString());
        // FIXME: The converter generator does not recognize that the sql encoder implementation already implements the methods getRepresentation(), isHashing(), isCompressing() and isEncryption().
        return JDBCQueryEncoderBuilder.withPreparedStatement(prepare(sqlStringBuilder.toString())).withRepresentation(Representation.INTERNAL).withHashing(false).withCompressing(false).withEncrypting(false).build();
    }
    
    /* -------------------------------------------------- Testing -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public @Nonnull ResultSet executeQuery(@Nonnull @SQLStatement String query) throws DatabaseException {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
