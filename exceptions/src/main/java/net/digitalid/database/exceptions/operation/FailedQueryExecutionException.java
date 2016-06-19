package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This exception is thrown when a filter could not be executed in the database.
 */
@Immutable
public class FailedQueryExecutionException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed filter execution exception.
     * 
     * @param cause the cause of the failed filter execution.
     */
    protected FailedQueryExecutionException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed filter execution exception.
     * 
     * @param cause the cause of the failed filter execution.
     * 
     * @return a new failed filter execution exception.
     */
    @Pure
    public static @Nonnull FailedQueryExecutionException get(@Nonnull SQLException cause) {
        return new FailedQueryExecutionException(cause);
    }
    
}
