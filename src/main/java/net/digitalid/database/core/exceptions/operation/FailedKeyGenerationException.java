package net.digitalid.database.core.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when the primary key could not be generated.
 */
@Immutable
public class FailedKeyGenerationException extends FailedNonCommittingOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed key generation exception.
     * 
     * @param cause the cause of the failed key generation.
     */
    protected FailedKeyGenerationException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed key generation exception.
     * 
     * @param cause the cause of the failed key generation.
     * 
     * @return a new failed key generation exception.
     */
    @Pure
    public static @Nonnull FailedKeyGenerationException get(@Nonnull SQLException cause) {
        return new FailedKeyGenerationException(cause);
    }
    
}
