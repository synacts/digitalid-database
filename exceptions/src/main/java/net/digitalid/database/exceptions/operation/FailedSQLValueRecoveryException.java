package net.digitalid.database.exceptions.operation;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * This exception is thrown when a value could not be restored from the database.
 */
@Immutable
// TODO: improve exception hierarchy
public class FailedSQLValueRecoveryException extends DatabaseException {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new failed value restoring exception.
     * 
     * @param cause the cause of the failed value restoring.
     */
    protected FailedSQLValueRecoveryException(@Nonnull SQLException cause) {
        super(cause);
    }
    
    /**
     * Returns a new failed value restoring exception.
     * 
     * @param cause the cause of the failed value restoring.
     * 
     * @return a new failed value restoring exception.
     */
    @Pure
    public static @Nonnull FailedSQLValueRecoveryException get(@Nonnull SQLException cause) {
        return new FailedSQLValueRecoveryException(cause);
    }
    
}
