package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This exception is thrown when a database resource could not be closed.
 */
@Immutable
public class FailedResourceClosingException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed resource closing exception.
     * 
     * @param cause the cause of the failed resource closing.
     */
    protected FailedResourceClosingException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed resource closing exception.
     * 
     * @param cause the cause of the failed resource closing.
     * 
     * @return a new failed resource closing exception.
     */
    @Pure
    public static @Nonnull FailedResourceClosingException get(@Nonnull SQLException cause) {
        return new FailedResourceClosingException(cause);
    }
    
}
