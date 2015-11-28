package net.digitalid.database.core.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.ColumnDeclaration;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.site.Site;
import net.digitalid.database.core.table.GeneralTable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class models foreign key references that are {@link Site site}-independent.
 */
@Immutable
public final class GeneralReference extends Reference {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Stores the database table whose column is referenced.
     */
    private final @Nonnull GeneralTable table;
    
    @Pure
    @Override
    public final @Nonnull GeneralTable getTable() {
        return table;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the specified table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
    protected GeneralReference(@Nonnull GeneralTable table, @Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        super(column, deleteOption, updateOption);
        
        this.table = table;
    }
    
    /**
     * Creates a new general column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the specified table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     * 
     * @return a new general column reference with the given parameters.
     */
    @Pure
    public static @Nonnull GeneralReference get(@Nonnull GeneralTable table, @Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        return new GeneralReference(table, column, deleteOption, updateOption);
    }
    
    /* -------------------------------------------------- Retrieval -------------------------------------------------- */
    
    @Pure
    @Override
    public final boolean isSiteSpecific() {
        return false;
    }
    
    @Locked
    @Override
    @NonCommitting
    public final @Nonnull String get(@Nullable Site site) throws FailedOperationException {
        return "REFERENCES " + table.getName() + " (" + getColumn().getName() + ") ON DELETE " + getDeleteOption() + " ON UPDATE " + getUpdateOption();
    }
    
}
