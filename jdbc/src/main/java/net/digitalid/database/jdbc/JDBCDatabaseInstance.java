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
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.type.Mutable;

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
import net.digitalid.database.jdbc.encoder.JDBCDataManipulationLanguageEncoderBuilder;
import net.digitalid.database.subject.site.Site;

/**
 * This classes uses the JDBC connection to execute the statements.
 */
@Mutable
public abstract class JDBCDatabaseInstance implements Database {
    
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
    
    // TODO: we need to figure out how we can initialize it
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
    // TODO: what properties are those?
    protected abstract @Nonnull Properties getProperties();
//    
//    /**
//     * Drops this database instance.
//     */
//    @Impure
//    @Committing
//    public void dropDatabase() throws DatabaseException {
//        
//    }
    
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
     * 
     * @return the database connection of the current thread.
     */
    @Impure
    @NonCommitting
    protected final @Nonnull Connection getConnection() throws DatabaseException {
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
        } catch (SQLException e) {
            throw DatabaseExceptionBuilder.withCause(e).build();
        }
    }
    
    @Impure
    @Override
    @Committing
    public void rollback() {
        try {
            getConnection().rollback();
            transaction.set(Boolean.FALSE);
        } catch (SQLException | DatabaseException exception) {
            Log.error("Could not roll back the transaction.", exception);
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
    private void executeStatement(@Nonnull Site<?> site, @Nonnull SQLTableStatement tableStatement) throws DatabaseException {
        final @Nonnull StringBuilder sqlStringBuilder = new StringBuilder();
        tableStatement.unparse(SQLDialect.instance.get(), site, sqlStringBuilder);
        try {
            // TODO: do we really need to set the result set type and result set concurrency?
            getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).execute(sqlStringBuilder.toString());
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull Site<?> site, @Nonnull SQLTableStatement tableStatement) throws DatabaseException {
        executeStatement(site, tableStatement);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull Site<?> site, @Nonnull SQLCreateTableStatement createTableStatement) throws DatabaseException {
        executeStatement(site, createTableStatement);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull Site site, @Nonnull SQLDropTableStatement dropTableStatement) throws DatabaseException {
        executeStatement(site, dropTableStatement);
    }
    
    /* -------------------------------------------------- Encoder -------------------------------------------------- */
    
    @PureWithSideEffects
    private @Nonnull SQLActionEncoder getEncoderForStatement(@Nonnull Site<?> site, @Nonnull SQLTableStatement tableStatement) throws DatabaseException {
        final @Nonnull StringBuilder sqlStringBuilder = new StringBuilder();
        tableStatement.unparse(SQLDialect.instance.get(), site, sqlStringBuilder);
        return JDBCDataManipulationLanguageEncoderBuilder.withPreparedStatement(prepare(sqlStringBuilder.toString())).build();
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws DatabaseException {
        return getEncoderForStatement(site, insertStatement);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull Site site, @Nonnull SQLUpdateStatement updateStatement) throws DatabaseException {
        return getEncoderForStatement(site, updateStatement);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull Site site, @Nonnull SQLDeleteStatement deleteStatement) throws DatabaseException {
        return getEncoderForStatement(site, deleteStatement);
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws DatabaseException {
        final @Nonnull StringBuilder sqlStringBuilder = new StringBuilder();
        selectStatement.unparse(SQLDialect.instance.get(), site, sqlStringBuilder);
//        return JDBCQueryEncoderBuilder.withPreparedStatement(prepare(sqlStringBuilder.toString())).build();
        return null;
    }
    
}
