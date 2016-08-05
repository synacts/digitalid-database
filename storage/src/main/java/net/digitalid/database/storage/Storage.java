package net.digitalid.database.storage;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.generation.Derive;
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
     * Returns the parent module to which this storage belongs.
     */
    @Pure
    public @Nullable Module getParentModule();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this storage.
     */
    @Pure
    public @Nonnull @CodeIdentifier String getName();
    
    /**
     * Returns the full name of this storage with the given delimiter between the names of the parent modules in the given direction.
     */
    @Pure
    public default @Nonnull @CodeIdentifier String getFullName(@Nonnull @CodeIdentifier String delimiter, boolean rootToLeaf) {
        if (getParentModule() != null) {
            final @Nonnull String parentName = getParentModule().getFullName(delimiter, rootToLeaf);
            return rootToLeaf ? parentName + delimiter + getName() : getName() + delimiter + parentName;
        } else {
            return getName();
        }
    }
    
    /**
     * Returns the full name of this storage with periods between the names of the parent modules from leaf to root.
     */
    @Pure
    @Derive("getFullName(\".\", false)")
    public @Nonnull @CodeIdentifier String getFullNameWithPeriods();
    
    /**
     * Returns the full name of this storage with underlines between the names of the parent modules from root to leaf.
     */
    @Pure
    @Derive("getFullName(\"_\", true)")
    public @Nonnull @CodeIdentifier String getFullNameWithUnderlines();
    
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
