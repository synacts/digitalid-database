package net.digitalid.database.conversion.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NullableElements;

/**
 *
 */
public class ConformityViolationException extends InternalException {
    
    protected ConformityViolationException(@Nonnull String message, @Nullable Exception cause, @Captured @Nonnull @NullableElements Object... arguments) {
        super(message, cause, arguments);
    }
    
    @Pure
    public static @Nonnull ConformityViolationException with(@Nonnull String message, @Nullable Exception cause, @Captured @Nonnull @NullableElements Object... arguments) {
        return new ConformityViolationException(message, cause, arguments);
    }
    
    @Pure
    public static @Nonnull ConformityViolationException with(@Nonnull String message, @Captured @Nonnull @NullableElements Object... arguments) {
        return new ConformityViolationException(message, null, arguments);
    }
    
}
