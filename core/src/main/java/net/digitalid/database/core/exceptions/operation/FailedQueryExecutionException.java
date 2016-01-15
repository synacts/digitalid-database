package net.digitalid.database.core.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This exception is thrown when a query could not be executed in the database.
 */
@Immutable
public class FailedQueryExecutionException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed query execution exception.
     * 
     * @param cause the cause of the failed query execution.
     */
    protected FailedQueryExecutionException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed query execution exception.
     * 
     * @param cause the cause of the failed query execution.
     * 
     * @return a new failed query execution exception.
     */
    @Pure
    public static @Nonnull FailedQueryExecutionException get(@Nonnull SQLException cause) {
        return new FailedQueryExecutionException(cause);
    }
    
}
