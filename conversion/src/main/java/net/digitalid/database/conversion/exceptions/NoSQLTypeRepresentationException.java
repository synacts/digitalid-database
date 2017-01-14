package net.digitalid.database.conversion.exceptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.exceptions.InternalException;

/**
 * Thrown if a converter cannot represent a type as a SQL type, e.g. a complex convertible or a non-embedded iterable.
 */
public class NoSQLTypeRepresentationException extends InternalException {
    
    /**
     * Constructs a new no-SQLType-representation exception with a given message and a given cause.
     */
    protected NoSQLTypeRepresentationException(@Nonnull String message, @Nullable Exception cause) {
        super(message, cause);
    }
    
    /**
     * Returns a new no-SQLType-representation exception with a given message and a given cause.
     */
    @Pure
    public static @Nonnull NoSQLTypeRepresentationException with(@Nonnull String message, @Nullable Exception cause) {
        return new NoSQLTypeRepresentationException(message, cause);
    }
    
    /**
     * Returns a new no-SQLType-representation exception with a given message.
     */
    @Pure
    public static @Nonnull NoSQLTypeRepresentationException with(@Nonnull String message) {
        return new NoSQLTypeRepresentationException(message, null);
    }
    
    @Pure
    public static @Nonnull NoSQLTypeRepresentationException with(@Nonnull Converter<?, ?> converter) {
        return new NoSQLTypeRepresentationException("The type represented by the converter '" + converter.getClass().getSimpleName() + "' cannot be represented as an SQL type", null);
    }
    
}
