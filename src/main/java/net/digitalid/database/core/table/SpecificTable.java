package net.digitalid.database.core.table;

import javax.annotation.Nonnull;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.site.Site;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;

/**
 * This class models a database table that is {@link Site site}-dependent.
 */
@Immutable
public abstract class SpecificTable extends Table {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new specific table with the given name and declaration.
     * 
     * @param name the site-dependent name of the new table.
     * @param declaration the declaration of the new table.
     */
    protected SpecificTable(@Nonnull @Validated String name, @Nonnull Declaration declaration) {
        super(name, declaration);
    }
    
    /* -------------------------------------------------- Site-Specific -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isSiteSpecific() {
        return true;
    }
    
}
