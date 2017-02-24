package net.digitalid.database.interfaces;

import java.sql.ResultSet;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.sql.SQLStatement;
import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.unit.Unit;

/**
 * This interface allows to execute SQL statements.
 */
@Mutable
public interface Database extends AutoCloseable {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    /**
     * Stores the database instance which is configured to handle all SQL executions.
     */
    public static final @Nonnull Configuration<Database> instance = Configuration.withUnknownProvider();
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Commits all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Impure
    @Committing
    public void commit() throws DatabaseException;
    
    /**
     * Rolls back all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Impure
    @Committing
    public void rollback();
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    /**
     * Executes the given SQL create table statement on the given unit.
     */
    @Impure
    public abstract void execute(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Executes the given drop table statement on the given unit.
     */
    @Impure
    public void execute(@Nonnull SQLDropTableStatement dropTableStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Returns an SQL action encoder for encoding the parameterized values of the given insert statement and executing it afterwards on the given unit.
     */
    @Pure
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLInsertStatement insertStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Returns an SQL action encoder for encoding the parameterized values of the given update statement and executing it afterwards on the given unit.
     */
    @Pure
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLUpdateStatement updateStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Returns an SQL action encoder for encoding the parameterized values of the given delete statement and executing it afterwards on the given unit.
     */
    @Pure
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLDeleteStatement deleteStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Returns an SQL query encoder for encoding the parameterized values of the given select statement and executing it afterwards on the given unit.
     */
    @Pure
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull SQLSelectStatement selectStatement, @Nonnull Unit unit) throws DatabaseException;
    
    /* -------------------------------------------------- Testing -------------------------------------------------- */
    
    /**
     * Executes the given SQL query.
     * This method should only be executed by tests that need
     * to verify that the database data was properly manipulated.
     */
    @PureWithSideEffects
    public @Nonnull ResultSet executeQuery(@Nonnull @SQLStatement String query) throws DatabaseException;
    
}
