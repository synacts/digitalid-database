package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * This class models a module whose tables can be created and deleted.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class Module extends RootClass implements Storage {
    
    /* -------------------------------------------------- Substorages -------------------------------------------------- */
    
    private final @Nonnull @NonFrozen FreezableList<@Nonnull Storage> substorages = FreezableLinkedList.withNoElements();
    
    /**
     * Returns the substorages of this storage.
     */
    @Pure
    public @Nonnull @NonFrozen ReadOnlyList<@Nonnull Storage> getSubstorages() {
        return substorages;
    }
    
    /**
     * Registers the given substorage at this storage.
     */
    @Impure
    final void addSubstorage(@Nonnull Storage substorage) {
        substorages.add(substorage);
    }
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    @Pure
    @Override
    @CallSuper
    protected void initialize() {
        final @Nullable Module module = getModule();
        if (module != null) { module.addSubstorage(this); }
        super.initialize();
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Impure
    @Override
    @NonCommitting
    public void createTables(@Nonnull Site site) throws DatabaseException {
        for (@Nonnull Storage substorage : substorages) {
            substorage.createTables(site);
        }
    }
    
    @Impure
    @Override
    @NonCommitting
    public void deleteTables(@Nonnull Site site) throws DatabaseException {
        for (@Nonnull Storage substorage : substorages) {
            substorage.deleteTables(site);
        }
    }
    
}
