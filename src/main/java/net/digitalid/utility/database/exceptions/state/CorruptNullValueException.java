package net.digitalid.utility.database.exceptions.state;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a value which may not be null is null.
 */
@Immutable
public class CorruptNullValueException extends CorruptStateException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new corrupt null value exception.
     */
    protected CorruptNullValueException() {
        super("An element which may not be null is null.");
    }
    
    /**
     * Returns a new corrupt null value exception.
     * 
     * @return a new corrupt null value exception.
     */
    @Pure
    public static @Nonnull CorruptNullValueException get() {
        return new CorruptNullValueException();
    }
    
}
