package net.digitalid.database.dialect.identifier.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * An SQL qualified table.
 * 
 * @see SQLExplicitlyQualifiedTable
 * @see SQLImplicitlyQualifiedTable
 */
@Immutable
public interface SQLQualifiedTable extends SQLTable {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table of this qualified table.
     */
    @Pure
    public @Nonnull SQLTableName getTable();
    
}
