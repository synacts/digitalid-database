package net.digitalid.database.storage.injections;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.storage.Table;
import net.digitalid.database.storage.Site;

/**
 * The table creator creates a given table in the database.
 */
@Stateless
public interface TableCreator {
    
    /* -------------------------------------------------- Interface -------------------------------------------------- */
    
    /**
     * Creates the given table on the given site.
     */
    @Impure
    @NonCommitting
    public void createTable(@Nonnull Table table, @Nonnull Site site) throws DatabaseException;
    
    /* -------------------------------------------------- Configuration -------------------------------------------------- */
    
    /**
     * Stores the table creator, which has to be provided by the conversion package.
     */
    public static final @Nonnull Configuration<TableCreator> configuration = Configuration.withUnknownProvider();
    
    /* -------------------------------------------------- Static Access -------------------------------------------------- */
    
    /**
     * Creates the given table on the given site.
     */
    @Impure
    @NonCommitting
    public static void create(@Nonnull Table table, @Nonnull Site site) throws DatabaseException {
        configuration.get().createTable(table, site);
    }
    
}
