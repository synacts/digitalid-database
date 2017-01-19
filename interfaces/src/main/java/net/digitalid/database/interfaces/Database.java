package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.site.Site;

/**
 * This interface allows to execute SQL statements.
 */
@Mutable
public interface Database extends AutoCloseable {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    // TODO: Use our configuration class here to store an instance of this interface.
    
    /* -------------------------------------------------- Binary Stream -------------------------------------------------- */
    
    /**
     * Returns whether binary streams are supported by this database instance.
     */
    @Pure
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
    
    @Impure
    public abstract void execute(@Nonnull String sqlStatement) throws DatabaseException;
    
    @Impure
    public abstract void execute(@Nonnull SQLEncoder encoder) throws DatabaseException;
    
    @Impure
    public abstract @Nonnull SQLEncoder getEncoder(@Nonnull FiniteIterable<@Nonnull String> preparedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, ReadOnlyList<@Nonnull Integer> columnCountForGroup) throws DatabaseException;
    
    @Impure
    public @Nonnull SQLDecoder executeSelect(@Nonnull String selectStatement) throws DatabaseException;
    
    @Impure
    public @Nonnull SQLDecoder executeSelect(@Nonnull SQLEncoder encoder) throws DatabaseException;
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    // TODO: Maybe pass an SQLSchemaName instead of a site object already here.
    // TODO: Create an SQLDefinitionStatementEncoder interface that has a 'void execute()' method.
    // TODO: Create an SQLManipulationStatementEncoder interface that has a 'int execute()' method.
    // TODO: Create an SQLSelectStatementEncoder interface that has a '@Nonnull SQLDecoder execute()' method.
    
    @Impure
    public abstract @Nonnull SQLDefinitionStatementEncoder getEncoder(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Site<?> site) throws DatabaseException;
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Executes the given drop table statement at the given site.
     * 
     * @param site the site at which the statement is to be executed.
     * @param dropTableStatement the drop table statement to execute.
     */
    public void execute(@Nonnull Site site, @Nonnull SQLDropTableStatement dropTableStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given insert statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param insertStatement the insert statement to execute.
     */
    public void execute(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given update statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param updateStatement the update statement to execute.
     * 
     * @return the number of rows updated by the given statement.
     */
    public int execute(@Nonnull Site site, @Nonnull SQLUpdateStatement updateStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given delete statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param deleteStatement the delete statement to execute.
     * 
     * @return the number of rows deleted by the given statement.
     */
    public int execute(@Nonnull Site site, @Nonnull SQLDeleteStatement deleteStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given select statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param selectStatement the select statement to execute.
     * 
     * @return the rows that were selected by the given statement.
     */
    public @Nonnull SQLDecoder execute(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws FailedNonCommittingOperationException, InternalException;
    
    // TODO: probably remove this.
    /**
     * Executes the given insert statement at the given site.
     * 
     * @param insertStatement the insert statement to execute.
     * 
     * @return the key that was generated by the given statement.
     */
    @Impure
    public long executeAndReturnGeneratedKey(@Nonnull String insertStatement) throws DatabaseException;
    
}
