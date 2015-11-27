package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when an object could not be restored from the database.
 */
@Immutable
public class FailedRestoringException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed restoring exception.
     * 
     * @param cause the cause of the failed restoring.
     */
    protected FailedRestoringException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed restoring exception.
     * 
     * @param cause the cause of the failed restoring.
     * 
     * @return a new failed restoring exception.
     */
    @Pure
    public static @Nonnull FailedRestoringException get(@Nonnull SQLException cause) {
        return new FailedRestoringException(cause);
    }
    
}
