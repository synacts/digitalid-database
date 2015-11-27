package net.digitalid.utility.database.exceptions.state.row;

import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.exceptions.state.CorruptStateException;

/**
 * This exception is thrown when the expected row count of an update is different than the encountered row count.
 * 
 * @see EntryNotDeletedException
 * @see EntryNotFoundException
 * @see EntryNotUpdatedException
 */
@Immutable
public abstract class WrongRowCountException extends CorruptStateException {
    
    /* -------------------------------------------------- Expected Row Count -------------------------------------------------- */
    
    /**
     * Stores the expected row count.
     */
    private final int expectedRowCount;
    
    /**
     * Returns the expected row count.
     * 
     * @return the expected row count.
     */
    @Pure
    public final int getExpectedRowCount() {
        return expectedRowCount;
    }
    
    /* -------------------------------------------------- Encountered Row Count -------------------------------------------------- */
    
    /**
     * Stores the encountered row count.
     */
    private final int encounteredRowCount;
    
    /**
     * Returns the encountered row count.
     * 
     * @return the encountered row count.
     */
    @Pure
    public final int getEncounteredRowCount() {
        return encounteredRowCount;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new corrupt row count exception.
     * 
     * @param expectedRowCount the expected row count.
     * @param encounteredRowCount the encountered row count.
     */
    protected WrongRowCountException(int expectedRowCount, int encounteredRowCount) {
        super("A row count of " + expectedRowCount + " was expected but a row count of " + encounteredRowCount + " was encountered.");
        
        this.expectedRowCount = expectedRowCount;
        this.encounteredRowCount = encounteredRowCount;
    }
    
}
