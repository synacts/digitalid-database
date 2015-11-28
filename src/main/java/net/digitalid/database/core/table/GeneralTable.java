package net.digitalid.database.core.table;

import javax.annotation.Nonnull;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.site.Site;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;

/**
 * This class models a database table that is {@link Site site}-independent.
 */
@Immutable
public final class GeneralTable extends Table {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general table with the given name and declaration.
     * 
     * @param name the site-independent name of the new table.
     * @param declaration the declaration of the new table.
     * 
     * @require !declaration.isSiteSpecific() : "The declaration is not site-specific.";
     */
    private GeneralTable(@Nonnull @Validated String name, @Nonnull Declaration declaration) {
        super(name, declaration);
    }
    
    /**
     * Returns a new general table with the given name and declaration.
     * 
     * @param name the site-independent name of the new table.
     * @param declaration the declaration of the new table.
     * 
     * @return a new general table with the given name and declaration.
     * 
     * @require !declaration.isSiteSpecific() : "The declaration is not site-specific.";
     */
    @Pure
    public static @Nonnull GeneralTable get(@Nonnull @Validated String name, @Nonnull Declaration declaration) {
        return new GeneralTable(name, declaration);
    }
    
    /* -------------------------------------------------- Site-Specific -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isSiteSpecific() {
        return false;
    }
    
}
