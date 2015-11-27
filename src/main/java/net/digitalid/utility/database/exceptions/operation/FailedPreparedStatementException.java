package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a prepared statement could not be created.
 */
@Immutable
public class FailedPreparedStatementException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed prepared statement exception.
     * 
     * @param cause the cause of the failed prepared statement.
     */
    protected FailedPreparedStatementException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed prepared statement exception.
     * 
     * @param cause the cause of the failed prepared statement.
     * 
     * @return a new failed prepared statement exception.
     */
    @Pure
    public static @Nonnull FailedPreparedStatementException get(@Nonnull SQLException cause) {
        return new FailedPreparedStatementException(cause);
    }
    
}
