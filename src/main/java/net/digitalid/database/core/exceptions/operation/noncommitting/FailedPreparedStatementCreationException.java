package net.digitalid.database.core.exceptions.operation.noncommitting;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a prepared statement could not be created.
 */
@Immutable
public class FailedPreparedStatementCreationException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed prepared statement creation exception.
     * 
     * @param cause the cause of the failed prepared statement creation.
     */
    protected FailedPreparedStatementCreationException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed prepared statement creation exception.
     * 
     * @param cause the cause of the failed prepared statement creation.
     * 
     * @return a new failed prepared statement creation exception.
     */
    @Pure
    public static @Nonnull FailedPreparedStatementCreationException get(@Nonnull SQLException cause) {
        return new FailedPreparedStatementCreationException(cause);
    }
    
}
