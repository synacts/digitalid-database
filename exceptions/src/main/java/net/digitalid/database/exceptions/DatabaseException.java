package net.digitalid.database.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.exceptions.operation.FailedOperationException;
import net.digitalid.database.exceptions.state.CorruptStateException;

/**
 * This exception indicates a database problem.
 * 
 * @see CorruptStateException
 * @see FailedOperationException
 */
@Immutable
public abstract class DatabaseException extends ExternalException {
    
    protected DatabaseException(@Nullable String message, @Nullable Exception cause, @Captured @Nonnull @NullableElements Object... arguments) {
        super(message, cause, arguments);
    }
    
    protected DatabaseException(@Nullable String message, @Captured @Nonnull @NullableElements Object... arguments) {
        super(message, arguments);
    }
    
    protected DatabaseException(@Nullable Exception cause) {
        super(cause);
    }
    
}
