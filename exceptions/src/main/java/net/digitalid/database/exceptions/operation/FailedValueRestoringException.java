package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This exception is thrown when a value could not be restored from the database.
 */
@Immutable
public class FailedValueRestoringException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed value restoring exception.
     * 
     * @param cause the cause of the failed value restoring.
     */
    protected FailedValueRestoringException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed value restoring exception.
     * 
     * @param cause the cause of the failed value restoring.
     * 
     * @return a new failed value restoring exception.
     */
    @Pure
    public static @Nonnull FailedValueRestoringException get(@Nonnull SQLException cause) {
        return new FailedValueRestoringException(cause);
    }
    
}
