package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLDataManipulationLanguageEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.subject.site.Site;

/**
 * This interface allows to execute SQL statements.
 */
@Mutable
public interface Database extends AutoCloseable {
    
    /* -------------------------------------------------- Configuration -------------------------------------------------- */
    
    /**
     * Stores the private key retriever, which has to be provided by the host package.
     */
    public static final @Nonnull Configuration<Database> configuration = Configuration.withUnknownProvider();
    
    /* -------------------------------------------------- Binary Stream -------------------------------------------------- */
    
    // TODO: Do we really need this method?
    /**
     * Returns whether binary streams are supported by this database instance.
     */
    @Pure
    @Deprecated
    public boolean supportsBinaryStreams();
    
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
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    // TODO: maybe we can remove this generic method.
    @Impure
    public abstract void execute(@Nonnull Site<?> site, @Nonnull SQLTableStatement tableStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    // TODO: Maybe pass an SQLSchemaName instead of a site object already here.
    // TODO: Create an SQLManipulationStatementEncoder interface that has a 'void execute()' method.
    // TODO: Create an SQLSelectStatementEncoder interface that has a '@Nonnull SQLDecoder execute()' method.
    
    /**
     * Executes an SQL Create Table Statement on a specific site.
     * 
     * @param site
     * @param createTableStatement
     * 
     * @throws DatabaseException
     */
    @Impure
    public abstract void execute(@Nonnull Site<?> site, @Nonnull SQLCreateTableStatement createTableStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Executes the given drop table statement at the given site.
     * 
     * @param site the site at which the statement is to be executed.
     * @param dropTableStatement the drop table statement to execute.
     */
    @Impure
    public void execute(@Nonnull Site site, @Nonnull SQLDropTableStatement dropTableStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Insert Into Table -------------------------------------------------- */
    
    /**
     * Returns an SQL Data Manipulation Language Encoder for an SQL Insert Statement, which can be passed to the converter 
     * in order to collect values for the parameterized statement. The SQL Data Manipulation Language Encoder can directly be executed.
     * 
     * @param site the site at which the statement is executed.
     * @param insertStatement the insert statement to execute.
     */
    @Impure
    public @Nonnull SQLDataManipulationLanguageEncoder getEncoder(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Returns an SQL Data Manipulation Language Encoder for an SQL Update Statement, which can be passed to the converter 
     * in order to collect values for the parameterized statement. The SQL Data Manipulation Language Encoder can directly be executed.
     * 
     * @param site the site at which the statement is executed.
     * @param updateStatement the update statement to execute.
     */
    @Impure
    public @Nonnull SQLDataManipulationLanguageEncoder getEncoder(@Nonnull Site site, @Nonnull SQLUpdateStatement updateStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Returns an SQL Data Manipulation Language Encoder for an SQL Delete Statement, which can be passed to the converter 
     * in order to collect values for the parameterized statement. The SQL Data Manipulation Language Encoder can directly be executed.
     * 
     * @param site the site at which the statement is executed.
     * @param deleteStatement the delete statement to execute.
     */
    @Impure
    public @Nonnull SQLDataManipulationLanguageEncoder getEncoder(@Nonnull Site site, @Nonnull SQLDeleteStatement deleteStatement) throws DatabaseException;
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Returns an SQL Query Encoder for an SQL Select Statement, which can be passed to the converter in order to collect values 
     * for the parameterized statement. The SQL Query Encoder can directly be executed.
     * 
     * @param site the site at which the statement is executed.
     * @param selectStatement the select statement to execute.
     */
    @Impure
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws DatabaseException;
    
}
