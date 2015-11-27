package net.digitalid.utility.database.exceptions.state.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.database.exceptions.state.CorruptStateException;

/**
 * This exception indicates a corrupt value.
 * 
 * @see CorruptNullValueException
 * @see CorruptParameterValueCombinationException
 * @see CorruptParameterValueException
 */
@Immutable
public abstract class CorruptValueException extends CorruptStateException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new corrupt value exception with the given message and cause.
     * 
     * @param message a string explaining the problem which has occurred.
     * @param cause the exception that caused this problem, if available.
     */
    protected CorruptValueException(@Nonnull String message, @Nullable Exception cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new corrupt value exception with the given message.
     * 
     * @param message a string explaining the problem which has occurred.
     */
    protected CorruptValueException(@Nonnull String message) {
        this(message, null);
    }
    
}
