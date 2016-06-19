package net.digitalid.database.exceptions.state.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This exception is thrown when a value which may not be null is null.
 */
@Immutable
// TODO: improve exception hierarchy
public class CorruptNullValueException extends RuntimeException {
    
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
