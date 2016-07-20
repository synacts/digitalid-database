package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.conversion.exceptions.FailedValueConversionException;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.annotations.method.Pure;

/**
 * This exception is thrown when an object could not be stored into the database.
 */
@Immutable
public class FailedSQLValueConversionException extends FailedValueConversionException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed value storing exception.
     * 
     * @param cause the cause of the failed value storing.
     */
    protected FailedSQLValueConversionException(@Nonnull SQLException cause, @Nonnull @Captured @NullableElements Object... arguments) {
        super("Failed to store data to the SQL database", cause, arguments);
    }
    
    /**
     * Returns a new failed value storing exception.
     * 
     * @param cause the cause of the failed value storing.
     * 
     * @return a new failed value storing exception.
     */
    @Pure
    public static @Nonnull FailedSQLValueConversionException get(@Nonnull SQLException cause, @Nonnull @Captured @NullableElements Object... arguments) {
        return new FailedSQLValueConversionException(cause, arguments);
    }
    
}
