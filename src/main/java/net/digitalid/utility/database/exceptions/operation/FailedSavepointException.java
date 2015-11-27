package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a savepoint could not be set or rolled back to.
 */
@Immutable
public class FailedSavepointException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed savepoint exception.
     * 
     * @param cause the cause of the failed savepoint.
     */
    protected FailedSavepointException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed savepoint exception.
     * 
     * @param cause the cause of the failed savepoint.
     * 
     * @return a new failed savepoint exception.
     */
    @Pure
    public static @Nonnull FailedSavepointException get(@Nonnull SQLException cause) {
        return new FailedSavepointException(cause);
    }
    
}
