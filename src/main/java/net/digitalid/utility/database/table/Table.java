package net.digitalid.utility.database.table;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.declaration.Declaration;
import net.digitalid.utility.database.site.Site;

/**
 * This class models a database table.
 */
@Immutable
public abstract class Table {
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Stores the declaration of this table.
     */
    private final @Nonnull Declaration declaration;
    
    /**
     * Returns the declaration of this table.
     * 
     * @return the declaration of this table.
     */
    @Pure
    public final @Nonnull Declaration getDeclaration() {
        return declaration;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new table with the given declaration.
     * 
     * @param declaration the declaration of the new table.
     */
    protected Table(@Nonnull Declaration declaration) {
        this.declaration = declaration;
    }
    
    /* -------------------------------------------------- Other -------------------------------------------------- */
    
    // TODO: Implement the creation and deletion of this table here.
    
    // TODO: Introduce an abstract isSiteSpecific() method to use as a precondition when the site parameter can be nullable.
    
    /**
     * Returns whether this table is {@link Site site}-specific.
     * 
     * @return whether this table is {@link Site site}-specific.
     */
    @Pure
    public abstract boolean isSiteSpecific();
    
    /**
     * Returns the name of this table with the prefix of the given site.
     * 
     * @param site the site whose prefix is to be used for the returned name.
     * 
     * @return the name of this table with the prefix of the given site.
     */
    @Pure
    public abstract @Nonnull @Validated String getName(@Nullable Site site);
    
    /**
     * Creates this table for the given site.
     * 
     * @param site the site for which to create this table.
     */
    @Locked
    @NonCommitting
    public abstract void create(@Nonnull Site site) throws SQLException;
    
}
