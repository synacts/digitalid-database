package net.digitalid.database.exceptions;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.exceptions.ConnectionException;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * A database exception indicates an interrupted connection to the database or violated SQL constraints.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class DatabaseException extends ConnectionException {
    
    /* -------------------------------------------------- Cause -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull SQLException getCause();
    
}
