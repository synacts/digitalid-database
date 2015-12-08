package net.digitalid.database.core.interfaces;

import javax.annotation.Nonnull;
import net.digitalid.database.core.annotations.Committing;
import net.digitalid.database.core.exceptions.operation.FailedCommitException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedNonCommittingOperationException;
import net.digitalid.database.core.sql.statement.delete.SQLDeleteStatement;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.core.sql.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.core.sql.statement.update.SQLUpdateStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This interface allows to execute SQL statements.
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
     * Executes the given statement at the given site.
     */
    public @Nonnull void execute(@Nonnull Site site, @Nonnull SQLCreateTableStatement createTableStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull void execute(@Nonnull Site site, @Nonnull SQLDropTableStatement dropTableStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull void execute(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull int executeAndReturnGeneratedKey(@Nonnull Site site, @Nonnull SQLInsertStatement insertStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull int execute(@Nonnull Site site, @Nonnull SQLUpdateStatement updateStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull int execute(@Nonnull Site site, @Nonnull SQLDeleteStatement deleteStatement) throws FailedNonCommittingOperationException, InternalException;
    
    /**
     * Executes the given statement at the given site.
     */
    public @Nonnull SelectionResult execute(@Nonnull Site site, @Nonnull SQLSelectStatement selectStatement) throws FailedNonCommittingOperationException, InternalException;
    
}
