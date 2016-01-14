package net.digitalid.database.core.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.state.CorruptStateException;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.exceptions.DigitalIDException;

/**
 * This exception indicates a database problem.
 * 
 * @see CorruptStateException
 * @see FailedOperationException
 */
@Immutable
public abstract class DatabaseException extends DigitalIDException {
    
    /**
     * Creates a new database exception with the given message and cause.
     * 
     * @param message a string explaining the problem which has occurred.
     * @param cause the exception that caused this problem, if available.
     */
    protected DatabaseException(@Nonnull String message, @Nullable Exception cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new database exception with the given message.
     * 
     * @param message a string explaining the problem which has occurred.
     */
    protected DatabaseException(@Nonnull String message) {
        super(message);
    }
    
}
