package net.digitalid.utility.database.reference;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.declaration.ColumnDeclaration;
import net.digitalid.utility.database.site.Site;
import net.digitalid.utility.database.table.GeneralTable;

/**
 * This class models foreign key references that are {@link Site site}-independent.
 */
@Immutable
public final class GeneralReference extends Reference {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the given table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
    private GeneralReference(@Nonnull GeneralTable table, @Nonnull ColumnDeclaration<?, ?> column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
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
    public static @Nonnull GeneralReference get(@Nonnull GeneralTable table, @Nonnull ColumnDeclaration<?, ?> column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        return new GeneralReference(table, column, deleteOption, updateOption);
    }
    
}
