package net.digitalid.utility.database.exceptions.operation.noncommitting;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.database.exceptions.operation.FailedOperationException;

/**
 * This exception indicates a failed database operation.
 * 
 * @see FailedKeyGenerationException
 * @see FailedPreparedStatementCreationException
 * @see FailedQueryExecutionException
 * @see FailedSavepointCreationException
 * @see FailedSavepointRollbackException
 * @see FailedStatementCreationException
 * @see FailedUpdateExecutionException
 * @see FailedValueRestoringException
 * @see FailedValueStoringException
 */
@Immutable
public abstract class FailedNonCommittingOperationException extends FailedOperationException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed non-committing operation exception with the given cause.
     * 
     * @param cause the SQL exception that caused the failed non-committing operation.
     */
    protected FailedNonCommittingOperationException(@Nonnull SQLException cause) {
        super(cause);
    }
    
}
