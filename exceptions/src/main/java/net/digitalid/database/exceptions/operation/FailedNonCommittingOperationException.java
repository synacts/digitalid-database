package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This exception indicates a failed database operation.
 * 
 * @see FailedConnectionException
 * @see FailedKeyGenerationException
 * @see FailedQueryExecutionException
 * @see FailedStatementCreationException
 * @see FailedUpdateExecutionException
 * @see FailedSQLValueRecoveryException
 * @see FailedSQLValueConversionException
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
