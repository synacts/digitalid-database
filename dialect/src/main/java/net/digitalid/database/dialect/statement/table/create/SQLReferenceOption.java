package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.unit.Unit;

/**
 * This class enumerates the available delete and update options.
 */
@Immutable
public enum SQLReferenceOption implements SQLNode {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * Rejects the delete or update operation for the parent table.
     */
    RESTRICT,
    
    /**
     * Propagates the delete or update operation to the child table.
     */
    CASCADE,
    
    /**
     * Sets the foreign key column or columns in the child table to NULL.
     */
    SET_NULL,
    
    /**
     * The same as {@link #RESTRICT} (at least in case of MySQL).
     */
    NO_ACTION;
    
    /* -------------------------------------------------- String -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull String toString() {
        return name().replace("_", " ");
    }
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(this);
    }
    
}
