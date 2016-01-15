package net.digitalid.database.core.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.annotations.Committing;
import net.digitalid.database.core.exceptions.operation.FailedCommitException;
import net.digitalid.database.core.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.core.interfaces.jdbc.JDBCDatabaseInstance;
import net.digitalid.database.core.sql.statement.delete.SQLDeleteStatement;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.core.sql.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.core.sql.statement.update.SQLUpdateStatement;
import net.digitalid.database.core.table.Site;

/**
 * This interface allows to execute SQL statements.
 * 
 * @see JDBCDatabaseInstance
 */
public interface DatabaseInstance extends AutoCloseable {
    
    /* -------------------------------------------------- Binary Stream -------------------------------------------------- */
    
    /**
     * Returns whether binary streams are supported by this database instance.
     * 
     * @return whether binary streams are supported by this database instance.
     */
    @Pure
    public boolean supportsBinaryStreams();
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Commits all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Committing
    public void commit() throws FailedCommitException;
    
    /**
     * Rolls back all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Committing
    public void rollback();
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    /**
     * Executes the given create table statement at the given site.
     * 
     * @param site the site at which the statement is to be executed.
     * @param createTableStatement the create table statement to execute.
     */
    public void execute(@Nonnull Site site, @Nonnull SQLCreateTableStatement createTableStatement) throws FailedNonCommittingOperationException, InternalException;
    
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
    public @Nonnull SelectionResult execute(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given insert statement at the given site.
     * 
     * @param site the site at which the statement is executed.
     * @param insertStatement the insert statement to execute.
     * 
     * @return the key that was generated by the given statement.
     */
    public long executeAndReturnGeneratedKey(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException;
    
}
