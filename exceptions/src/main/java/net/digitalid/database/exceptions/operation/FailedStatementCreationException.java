package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This exception is thrown when a statement could not be created.
 */
@Immutable
public class FailedStatementCreationException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed statement creation exception.
     * 
     * @param cause the cause of the failed statement creation.
     */
    protected FailedStatementCreationException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed statement creation exception.
     * 
     * @param cause the cause of the failed statement creation.
     * 
     * @return a new failed statement creation exception.
     */
    @Pure
    public static @Nonnull FailedStatementCreationException get(@Nonnull SQLException cause) {
        return new FailedStatementCreationException(cause);
    }
    
}
