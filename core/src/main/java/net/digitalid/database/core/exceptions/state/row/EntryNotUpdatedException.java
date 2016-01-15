package net.digitalid.database.core.exceptions.state.row;

import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;

/**
 * This exception is thrown when the expected row count of an update is different than the encountered row count.
 */
@Immutable
public class EntryNotUpdatedException extends WrongRowCountException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new entry not updated exception.
     * 
     * @param expectedRowCount the expected row count.
     * @param encounteredRowCount the encountered row count.
     */
    protected EntryNotUpdatedException(int expectedRowCount, int encounteredRowCount) {
        super(expectedRowCount, encounteredRowCount);
    }
    
    /**
     * Checks whether the number of rows updated by the statement equals the expected row count.
     * 
     * @param statement the (prepared) statement used execute the update.
     * @param expectedRowCount the expected row count of the update.
     * 
     * @throws EntryNotUpdatedException if this is not the case.
     */
    @Pure
    public static void check(@Nonnull Statement statement, int expectedRowCount) throws EntryNotUpdatedException, FailedUpdateExecutionException {
        try {
            final int encounteredRowCount = statement.getUpdateCount();
            if (encounteredRowCount != expectedRowCount) {
                throw new EntryNotUpdatedException(expectedRowCount, encounteredRowCount);
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
}
