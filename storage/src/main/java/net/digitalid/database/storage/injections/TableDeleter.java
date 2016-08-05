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
 * The table deleter deletes a given table in the database.
 */
@Stateless
public interface TableDeleter {
    
    /* -------------------------------------------------- Interface -------------------------------------------------- */
    
    /**
     * Deletes the given table on the given site.
     */
    @Impure
    @NonCommitting
    public void deleteTable(@Nonnull Table table, @Nonnull Site site) throws DatabaseException;
    
    /* -------------------------------------------------- Configuration -------------------------------------------------- */
    
    /**
     * Stores the table deleter, which has to be provided by the conversion package.
     */
    public static final @Nonnull Configuration<TableDeleter> configuration = Configuration.withUnknownProvider();
    
    /* -------------------------------------------------- Static Access -------------------------------------------------- */
    
    /**
     * Deletes the given table on the given site.
     */
    @Impure
    @NonCommitting
    public static void delete(@Nonnull Table table, @Nonnull Site site) throws DatabaseException {
        configuration.get().deleteTable(table, site);
    }
    
}
