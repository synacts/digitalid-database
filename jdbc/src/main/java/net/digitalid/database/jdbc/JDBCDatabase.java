package net.digitalid.database.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.sql.SQLStatement;
import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLStatementNode;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.schema.SQLCreateSchemaStatement;
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
 * This classes uses Java Database Connectivity (JDBC) to execute the statements.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
@TODO(task = "Maybe we need to support (optional) locking (for SQLite).", date = "2017-03-07", author = Author.KASPAR_ETTER)
public abstract class JDBCDatabase extends Database {
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    /**
     * Returns the JDBC driver of this database.
     */
    @Pure
    protected abstract @Nonnull Driver getDriver();
    
    /**
     * Returns the JDBC URL of this database.
     */
    @Pure
    protected abstract @Nonnull String getURL();
    
    /**
     * Returns the database user on whose behalf the connection is made.
     */
    @Pure
    protected abstract @Nullable String getUser();
    
    /**
     * Returns the password of the user or null if no password is needed.
     */
    @Pure
    protected abstract @Nullable String getPassword();
    
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
            final @Nonnull Connection connection;
            if (getUser() == null || getPassword() == null) { connection = DriverManager.getConnection(getURL()); }
            else { connection = DriverManager.getConnection(getURL(), getUser(), getPassword()); }
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
            Log.debugging("Committed the current transaction.");
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
            Log.debugging("Rolled back the current transaction.");
        } catch (@Nonnull SQLException | DatabaseException exception) {
            Log.error("Could not roll back the transaction.", exception);
        } finally {
            runRunnablesAfterRollback();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void close() throws Exception {
        getConnection().close();
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
    
    @PureWithSideEffects
    private void executeStatement(@Nonnull SQLStatementNode statement, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull String statementAsString = SQLDialect.unparse(statement, unit);
        Log.debugging("Executing $", statementAsString);
        try {
            // TODO: do we really need to set the result set type and result set concurrency?
            getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).execute(statementAsString);
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLCreateSchemaStatement createSchemaStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(createSchemaStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(createTableStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLDropTableStatement dropTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(dropTableStatement, unit);
    }
    
    /* -------------------------------------------------- Encoder -------------------------------------------------- */
    
    /**
     * Prepares the given statement at the given site.
     */
    @Pure
    protected @Nonnull PreparedStatement prepare(@Nonnull String statement) throws DatabaseException {
        try {
            return getConnection().prepareStatement(statement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
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
