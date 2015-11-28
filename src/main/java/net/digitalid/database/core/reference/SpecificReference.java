package net.digitalid.database.core.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.ColumnDeclaration;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.site.Site;
import net.digitalid.database.core.table.SpecificTable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class models foreign key references that are {@link Site site}-dependent.
 */
@Immutable
public final class SpecificReference extends Reference {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Stores the database table whose column is referenced.
     */
    private final @Nonnull SpecificTable table;
    
    @Pure
    @Override
    public final @Nonnull SpecificTable getTable() {
        return table;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new specific column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the specified table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
    protected SpecificReference(@Nonnull SpecificTable table, @Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        super(column, deleteOption, updateOption);
        
        this.table = table;
    }
    
    /**
     * Creates a new specific column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the specified table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     * 
     * @return a new specific column reference with the given parameters.
     */
    @Pure
    public static @Nonnull SpecificReference get(@Nonnull SpecificTable table, @Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        return new SpecificReference(table, column, deleteOption, updateOption);
    }
    
    /* -------------------------------------------------- Retrieval -------------------------------------------------- */
    
    @Pure
    @Override
    public final boolean isSiteSpecific() {
        return true;
    }
    
    @Locked
    @Override
    @NonCommitting
    public final @Nonnull String get(@Nullable Site site) throws FailedOperationException {
        assert site != null : "The site is not null.";
        
        table.create(site);
        return "REFERENCES " + table.getName(site) + " (entity, " + getColumn().getName() + ") ON DELETE " + getDeleteOption() + " ON UPDATE " + getUpdateOption();
    }
    
}
