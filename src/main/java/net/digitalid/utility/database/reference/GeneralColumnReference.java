package net.digitalid.utility.database.reference;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.converter.ColumnSQLConverter;
import net.digitalid.utility.database.site.Site;
import net.digitalid.utility.database.table.GeneralDatabaseTable;

/**
 * This class models foreign key references that are {@link Site site}-independent.
 */
@Immutable
public final class GeneralColumnReference extends ColumnReference {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the given table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
    private GeneralColumnReference(@Nonnull GeneralDatabaseTable table, @Nonnull ColumnSQLConverter<?, ?> column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        super(table, column, deleteOption, updateOption, false);
    }
    
    /**
     * Creates a new general column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the given table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     * 
     * @return a new general column reference with the given parameters.
     */
    @Pure
    public static @Nonnull GeneralColumnReference get(@Nonnull GeneralDatabaseTable table, @Nonnull ColumnSQLConverter<?, ?> column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        return new GeneralColumnReference(table, column, deleteOption, updateOption);
    }
    
}
