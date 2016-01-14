package net.digitalid.database.core.exceptions.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.exceptions.DatabaseException;
import net.digitalid.database.core.exceptions.state.row.WrongRowCountException;
import net.digitalid.utility.validation.state.Immutable;

/**
 * This exception indicates a corrupt database state.
 * 
 * @see CorruptValueException
 * @see WrongRowCountException
 * @see MaskingCorruptStateException
 */
@Immutable
public abstract class CorruptStateException extends DatabaseException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new corrupt state exception with the given message and cause.
     * 
     * @param message a string explaining the problem which has occurred.
     * @param cause the exception that caused this problem, if available.
     */
    protected CorruptStateException(@Nonnull String message, @Nullable Exception cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new corrupt state exception with the given message.
     * 
     * @param message a string explaining the problem which has occurred.
     */
    protected CorruptStateException(@Nonnull String message) {
        this(message, null);
    }
    
}
