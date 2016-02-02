package net.digitalid.database.conversion.exceptions;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;

/**
 * This exception indicates that the converted type does not comply with the expected coding style or class structure.
 */
public class ConformityViolation extends InternalException {
    
    protected ConformityViolation(String message, Exception cause) {
        super(message, cause);
    }
    
    public static @Nonnull ConformityViolation with(@Nonnull String message, @Nonnull Exception cause) {
        return new ConformityViolation(message, cause);
    }
    
}
