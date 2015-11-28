package net.digitalid.database.core.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when the connection to the database could not be closed.
 */
@Immutable
public class FailedClosingException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed closing exception.
     * 
     * @param cause the cause of the failed closing.
     */
    protected FailedClosingException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed closing exception.
     * 
     * @param cause the cause of the failed closing.
     * 
     * @return a new failed closing exception.
     */
    @Pure
    public static @Nonnull FailedClosingException get(@Nonnull SQLException cause) {
        return new FailedClosingException(cause);
    }
    
}
