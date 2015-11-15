package net.digitalid.utility.database.reference;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.declaration.ColumnDeclaration;
import net.digitalid.utility.database.site.Site;
import net.digitalid.utility.database.table.Table;

/**
 * This class models single-column foreign key references.
 * 
 * @see GeneralColumnReference
 */
@Immutable
public class Reference {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Stores the database table whose column is referenced.
     */
    private final @Nonnull Table table;
    
    /**
     * Returns the database table whose column is referenced.
     * 
     * @return the database table whose column is referenced.
     */
    @Pure
    public final @Nonnull Table getTable() {
        return table;
    }
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Stores the referenced column within the given table.
     */
    private final @Nonnull ColumnDeclaration column;
    
    /**
     * Returns the referenced column within the given table.
     * 
     * @return the referenced column within the given table.
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
    
    /* -------------------------------------------------- Entity Dependence -------------------------------------------------- */
    
    /**
     * Stores whether this reference depends on an entity.
     */
    private final boolean entityDependent;
    
    /**
     * Returns whether this reference depends on an entity.
     * 
     * @return whether this reference depends on an entity.
     */
    @Pure
    public final boolean isEntityDependent() {
        return entityDependent;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param column the referenced column within the given table.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     * @param entityDependent whether this reference depends on an entity.
     */
    protected Reference(@Nonnull Table table, @Nonnull ColumnDeclaration column, @Nonnull ReferenceOption deleteOption, @Nonnull ReferenceOption updateOption, boolean entityDependent) {
        this.table = table;
        this.column = column;
        this.deleteOption = deleteOption;
        this.updateOption = updateOption;
        this.entityDependent = entityDependent;
    }
    
    /* -------------------------------------------------- Retrieval -------------------------------------------------- */
    
    /**
     * Returns the reference to the table of the given site after creating it first.
     * 
     * @param site the site at which the foreign key constraint is declared and used.
     * 
     * @return the reference to the table of the given site after creating it first.
     * 
     * @require !table.isSiteSpecific() || site != null : "If the table is site-specific, the site may not be null.";
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    @Locked
    @NonCommitting
    public final @Nonnull String get(@Nullable Site site) throws SQLException {
        assert !table.isSiteSpecific() || site != null : "If the table is site-specific, the site may not be null.";
        
        table.create(site);
        return "REFERENCES " + table.getName(site) + " (" + (isEntityDependent() ? "entity, " : "") + column.getName() + ") ON DELETE " + deleteOption + " ON UPDATE " + updateOption;
    }
    
}
