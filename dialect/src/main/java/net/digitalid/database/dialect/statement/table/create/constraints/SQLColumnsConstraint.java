package net.digitalid.database.dialect.statement.table.create.constraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.column.SQLColumnName;

/**
 * A table constraint that references a list of columns.
 * 
 * @see SQLForeignKeyConstraint
 * @see SQLPrimaryKeyConstraint
 * @see SQLUniqueConstraint
 */
@Immutable
public interface SQLColumnsConstraint extends SQLTableConstraint {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<SQLColumnName> getColumns();
    
}
