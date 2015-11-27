package net.digitalid.utility.database.exceptions.operation.noncommitting;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a savepoint could not be rolled back to.
 */
@Immutable
public class FailedSavepointRollbackException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed savepoint rollback exception.
     * 
     * @param cause the cause of the failed savepoint rollback.
     */
    protected FailedSavepointRollbackException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed savepoint rollback exception.
     * 
     * @param cause the cause of the failed savepoint rollback.
     * 
     * @return a new failed savepoint rollback exception.
     */
    @Pure
    public static @Nonnull FailedSavepointRollbackException get(@Nonnull SQLException cause) {
        return new FailedSavepointRollbackException(cause);
    }
    
}
