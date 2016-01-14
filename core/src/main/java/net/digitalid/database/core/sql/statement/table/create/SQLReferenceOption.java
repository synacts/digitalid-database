package net.digitalid.database.core.sql.statement.table.create;

import javax.annotation.Nonnull;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This class enumerates the available delete and update options.
 * 
 * @see ColumnReference
 */
@Immutable
public enum SQLReferenceOption {
    
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
    
}
