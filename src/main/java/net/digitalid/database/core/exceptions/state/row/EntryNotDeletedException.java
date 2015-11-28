package net.digitalid.database.core.exceptions.state.row;

import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedUpdateExecutionException;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when the expected row count of a deletion is different than the encountered row count.
 */
@Immutable
public class EntryNotDeletedException extends WrongRowCountException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new entry not deleted exception.
     * 
     * @param expectedRowCount the expected row count.
     * @param encounteredRowCount the encountered row count.
     */
    protected EntryNotDeletedException(int expectedRowCount, int encounteredRowCount) {
        super(expectedRowCount, encounteredRowCount);
    }
    
    /**
     * Checks whether the number of rows deleted by the statement equals the expected row count.
     * 
     * @param statement the (prepared) statement used execute the deletion.
     * @param expectedRowCount the expected row count of the deletion.
     * 
     * @throws EntryNotDeletedException if this is not the case.
     */
    @Pure
    public static void check(@Nonnull Statement statement, int expectedRowCount) throws EntryNotDeletedException, FailedUpdateExecutionException {
        try {
            final int encounteredRowCount = statement.getUpdateCount();
            if (encounteredRowCount != expectedRowCount) {
                throw new EntryNotDeletedException(expectedRowCount, encounteredRowCount);
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
}
