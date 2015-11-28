package net.digitalid.database.core.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.ColumnDeclaration;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.site.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class models single-column foreign key references.
 * 
 * @see GeneralReference
 * @see SpecificReference
 */
@Immutable
public abstract class Reference {
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Stores the referenced column within the specified table.
     */
    private final @Nonnull ColumnDeclaration column;
    
    /**
     * Returns the referenced column within the specified table.
     * 
     * @return the referenced column within the specified table.
     */
    @Pure
    public final @Nonnull ColumnDeclaration getColumn() {
        return column;
    }
    
    /* -------------------------------------------------- Delete Option -------------------------------------------------- */
    
    /**
     * Stores the referential action triggered on deletion.
     */
    private final @Nonnull ReferenceOption deleteOption;
    
    /**
     * Returns the referential action triggered on deletion.
     * 
     * @return the referential action triggered on deletion.
     */
    @Pure
    public final @Nonnull ReferenceOption getDeleteOption() {
        return deleteOption;
    }
    
    /* -------------------------------------------------- Update Option -------------------------------------------------- */
    
    /**
     * Stores the referential action triggered on update.
     */
    private final @Nonnull ReferenceOption updateOption;
    
    /**
     * Returns the referential action triggered on update.
     * 
     * @return the referential action triggered on update.
     */
    @Pure
    public final @Nonnull ReferenceOption getUpdateOption() {
        return updateOption;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column reference with the given parameters.
     * 
     * @param column the referenced column within the specified table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
    protected Reference(@Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption) {
        this.column = column;
        this.deleteOption = deleteOption;
        this.updateOption = updateOption;
    }
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the database table whose column is referenced.
     * 
     * @return the database table whose column is referenced.
     */
    @Pure
    public abstract @Nonnull Table getTable();
    
    /* -------------------------------------------------- Retrieval -------------------------------------------------- */
    
    /**
     * Returns whether this reference is {@link Site site}-specific.
     * 
     * @return whether this reference is {@link Site site}-specific.
     */
    @Pure
    public abstract boolean isSiteSpecific();
    
    /**
     * Returns the reference to the table of the given site after creating it first.
     * 
     * @param site the site at which the foreign key constraint is declared and used.
     * 
     * @return the reference to the table of the given site after creating it first.
     * 
     * @require !isSiteSpecific() || site != null : "If this reference is site-specific, the site is not null.";
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    @Locked
    @NonCommitting
    public abstract @Nonnull String get(@Nullable Site site) throws FailedOperationException;
    
}
