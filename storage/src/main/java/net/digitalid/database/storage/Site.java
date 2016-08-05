package net.digitalid.database.storage;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.rootclass.RootClassWithException;
import net.digitalid.utility.validation.annotations.equality.Unequal;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.storage.Storage;

/**
 * This class allows the same code to work on both hosts and clients.
 */
@Mutable
public abstract class Site extends RootClassWithException<DatabaseException> {
    
    /* -------------------------------------------------- Queries -------------------------------------------------- */
    
    /**
     * Returns whether this site is a host.
     */
    @Pure
    public abstract boolean isHost();
    
    /**
     * Returns whether this site is a client.
     */
    @Pure
    public final boolean isClient() {
        return !isHost();
    }
    
    /* -------------------------------------------------- Database Name -------------------------------------------------- */
    
    /**
     * Returns the database name of this site.
     */
    @Pure
    public abstract @Nonnull @CodeIdentifier @MaxSize(63) @Unequal("general") String getDatabaseName();
    
    /* -------------------------------------------------- Entity Reference -------------------------------------------------- */
        
    /**
     * Returns the foreign key referenced by the entity column on this site.
     * 
     * TODO: The return type probably shouldn't be a string but rather an SQL syntax node?
     */
    @Pure
    public abstract @Nonnull String getEntityReference();
    
    /* -------------------------------------------------- Storages -------------------------------------------------- */
    
    private static final @Nonnull @NonFrozen FreezableList<@Nonnull Storage> storages = FreezableLinkedList.withNoElements();
    
    /**
     * Returns the storages of this site.
     */
    @Pure
    public static @Nonnull @NonFrozen ReadOnlyList<@Nonnull Storage> getStorages() {
        return storages;
    }
    
    /**
     * Registers the given substorage at this storage.
     */
    @Impure
    static final void addStorage(@Nonnull Storage storage) {
        storages.add(storage);
    }
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    @Pure
    @Override
    @CallSuper
    protected void initialize() throws DatabaseException {
        for (@Nonnull Storage storage : storages) {
            storage.createTables(this);
        }
        super.initialize();
    }
    
}
