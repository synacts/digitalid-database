package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This exception is thrown when a database transaction could not be committed.
 */
@Immutable
public class FailedCommitException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed commit exception.
     * 
     * @param cause the cause of the failed commit.
     */
    protected FailedCommitException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed commit exception with the given cause.
     */
    @Pure
    public static @Nonnull FailedCommitException get(@Nonnull SQLException cause) {
        return new FailedCommitException(cause);
    }
    
}
