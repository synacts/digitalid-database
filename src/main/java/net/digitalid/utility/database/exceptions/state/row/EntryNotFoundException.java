package net.digitalid.utility.database.exceptions.state.row;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.exceptions.operation.noncommitting.FailedQueryExecutionException;

/**
 * This exception is thrown when a query has not found the desired entry.
 */
@Immutable
public class EntryNotFoundException extends WrongRowCountException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new entry not found exception.
     */
    protected EntryNotFoundException() {
        super(1, 0);
    }
    
    /**
     * Checks whether the result set contains the queried entry.
     * 
     * @param resultSet the result set that should contain the entry.
     * 
     * @throws EntryNotFoundException if the entry could not be found.
     */
    @Pure
    public static void check(@Nonnull ResultSet resultSet) throws EntryNotFoundException, FailedQueryExecutionException {
        try {
            if (!resultSet.next()) {
                throw new EntryNotFoundException();
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedQueryExecutionException.get(exception);
        }
    }
    
}
