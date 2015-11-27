package net.digitalid.utility.database.exceptions.operation.noncommitting;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when an object could not be stored into the database.
 */
@Immutable
public class FailedValueStoringException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed value storing exception.
     * 
     * @param cause the cause of the failed value storing.
     */
    protected FailedValueStoringException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed value storing exception.
     * 
     * @param cause the cause of the failed value storing.
     * 
     * @return a new failed value storing exception.
     */
    @Pure
    public static @Nonnull FailedValueStoringException get(@Nonnull SQLException cause) {
        return new FailedValueStoringException(cause);
    }
    
}
