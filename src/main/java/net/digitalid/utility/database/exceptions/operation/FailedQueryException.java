package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a query could not be executed in the database.
 */
@Immutable
public class FailedQueryException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed query exception.
     * 
     * @param cause the cause of the failed query.
     */
    protected FailedQueryException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed query exception.
     * 
     * @param cause the cause of the failed query.
     * 
     * @return a new failed query exception.
     */
    @Pure
    public static @Nonnull FailedQueryException get(@Nonnull SQLException cause) {
        return new FailedQueryException(cause);
    }
    
}
