package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when no connection to the database could be established.
 */
@Immutable
public class FailedConnectionException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed connection exception.
     * 
     * @param cause the cause of the failed connection.
     */
    protected FailedConnectionException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed connection exception.
     * 
     * @param cause the cause of the failed connection.
     * 
     * @return a new failed connection exception.
     */
    @Pure
    public static @Nonnull FailedConnectionException get(@Nonnull SQLException cause) {
        return new FailedConnectionException(cause);
    }
    
}
