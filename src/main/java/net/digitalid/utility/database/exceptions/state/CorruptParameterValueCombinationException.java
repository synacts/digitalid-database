package net.digitalid.utility.database.exceptions.state;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This exception is thrown when a combination of parameter values is invalid.
 */
@Immutable
public class CorruptParameterValueCombinationException extends CorruptStateException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new corrupt combination exception with the given message.
     * 
     * @param message a string explaining which values cannot be combined.
     */
    protected CorruptParameterValueCombinationException(@Nonnull String message) {
        super(message);
    }
    
    /**
     * Returns a new corrupt combination exception with the given message.
     * 
     * @param message a string explaining which values cannot be combined.
     * 
     * @return a new corrupt combination exception with the given message.
     */
    @Pure
    public static @Nonnull CorruptParameterValueCombinationException get(@Nonnull String message) {
        return new CorruptParameterValueCombinationException(message);
    }
    
}
