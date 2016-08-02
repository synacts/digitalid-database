package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.generation.Normalize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * This interface models a storage whose tables can be created and deleted.
 * 
 * @see Module
 * @see Table
 */
@Mutable
public interface Storage extends RootInterface {
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    /**
     * Returns the module to which this storage belongs.
     */
    @Pure
    public @Nullable Module getModule();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this storage.
     */
    @Pure
    @Normalize("module == null ? name : module.getName() + \"_\" + name")
    public @Nonnull @CodeIdentifier String getName();
    
    /* -------------------------------------------------- Tables -------------------------------------------------- */
    
    /**
     * Creates the database tables of this storage on the given site.
     */
    @Impure
    @NonCommitting
    public void createTables(@Nonnull Site site) throws DatabaseException;
    
    /**
     * Deletes the database tables of this storage on the given site.
     */
    @Impure
    @NonCommitting
    public void deleteTables(@Nonnull Site site) throws DatabaseException;
    
}
