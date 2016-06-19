package net.digitalid.database.exceptions.state.row;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This exception is thrown when a filter has not found the desired entry.
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
     * Creates a new entry not found exception.
     */
    @Pure
    public static @Nonnull EntryNotFoundException get() {
        return new EntryNotFoundException();
    }
    
}
