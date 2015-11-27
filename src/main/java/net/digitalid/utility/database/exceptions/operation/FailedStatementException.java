package net.digitalid.utility.database.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a statement could not be created.
 */
@Immutable
public class FailedStatementException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed statement exception.
     * 
     * @param cause the cause of the failed statement.
     */
    protected FailedStatementException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed statement exception.
     * 
     * @param cause the cause of the failed statement.
     * 
     * @return a new failed statement exception.
     */
    @Pure
    public static @Nonnull FailedStatementException get(@Nonnull SQLException cause) {
        return new FailedStatementException(cause);
    }
    
}
