package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when an update could not be executed in the database.
 */
@Immutable
public class FailedUpdateException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed update exception.
     * 
     * @param cause the cause of the failed update.
     */
    protected FailedUpdateException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed update exception.
     * 
     * @param cause the cause of the failed update.
     * 
     * @return a new failed update exception.
     */
    @Pure
    public static @Nonnull FailedUpdateException get(@Nonnull SQLException cause) {
        return new FailedUpdateException(cause);
    }
    
}
