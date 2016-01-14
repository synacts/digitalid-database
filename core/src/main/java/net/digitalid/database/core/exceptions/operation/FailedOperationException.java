package net.digitalid.database.core.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.DatabaseException;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This exception indicates a failed database operation.
 * 
 * @see FailedCommitException
 * @see FailedClosingException
 * @see FailedNonCommittingOperationException
 */
@Immutable
public abstract class FailedOperationException extends DatabaseException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed operation exception with the given cause.
     * 
     * @param cause the SQL exception that caused the failed operation.
     */
    protected FailedOperationException(@Nonnull SQLException cause) {
        super("A database operation failed.", cause);
    }
    
    /* -------------------------------------------------- Cause -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SQLException getCause() {
        return (SQLException) super.getCause();
    }
    
}
