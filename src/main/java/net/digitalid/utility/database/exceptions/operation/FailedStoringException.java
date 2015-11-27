package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when an object could not be stored into the database.
 */
@Immutable
public class FailedStoringException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed storing exception.
     * 
     * @param cause the cause of the failed storing.
     */
    protected FailedStoringException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed storing exception.
     * 
     * @param cause the cause of the failed storing.
     * 
     * @return a new failed storing exception.
     */
    @Pure
    public static @Nonnull FailedStoringException get(@Nonnull SQLException cause) {
        return new FailedStoringException(cause);
    }
    
}
