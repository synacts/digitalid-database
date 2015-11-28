package net.digitalid.database.core.exceptions.operation;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.DatabaseException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedNonCommittingOperationException;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception indicates a failed database operation.
 * 
 * @see FailedClosingException
 * @see FailedCommitException
 * @see FailedConnectionException
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
