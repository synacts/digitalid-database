package net.digitalid.utility.database.exceptions.operation.noncommitting;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a savepoint could not be created.
 */
@Immutable
public class FailedSavepointCreationException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed savepoint creation exception.
     * 
     * @param cause the cause of the failed savepoint creation.
     */
    protected FailedSavepointCreationException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed savepoint creation exception.
     * 
     * @param cause the cause of the failed savepoint creation.
     * 
     * @return a new failed savepoint creation exception.
     */
    @Pure
    public static @Nonnull FailedSavepointCreationException get(@Nonnull SQLException cause) {
        return new FailedSavepointCreationException(cause);
    }
    
}
