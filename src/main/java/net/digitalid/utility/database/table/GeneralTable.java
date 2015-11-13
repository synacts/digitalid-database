package net.digitalid.utility.database.table;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.site.Site;

/**
 * This class models a database table that is {@link Site site}-independent.
 */
@Immutable
public final class GeneralTable implements Table {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Stores the name of this table.
     */
    private final @Nonnull String name;
    
    @Pure
    @Override
    public final @Nonnull @Validated String getName(@Nonnull Site site) {
        return name;
    }
    
    /* -------------------------------------------------- Creation -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public final void create(@Nonnull Site site) throws SQLException {
        // General database tables are created during the initialization of the corresponding class.
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general database table with the given name.
     * 
     * @param name the site-independent name of the new table.
     */
    private GeneralTable(@Nonnull String name) {
        this.name = name;
    }
    
    /**
     * Returns a new general database table with the given name.
     * 
     * @param name the site-independent name of the new table.
     * 
     * @return a new general database table with the given name.
     */
    @Pure
    public static @Nonnull GeneralTable get(@Nonnull String name) {
        return new GeneralTable(name);
    }
    
}
