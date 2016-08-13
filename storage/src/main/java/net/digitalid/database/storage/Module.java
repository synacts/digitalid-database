package net.digitalid.database.storage;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This class models an inner node in the storage tree.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class Module extends Storage {
    
    /* -------------------------------------------------- Substorages -------------------------------------------------- */
    
    private final @Nonnull @NonFrozen FreezableList<@Nonnull Storage> childStorages = FreezableLinkedList.withNoElements();
    
    /**
     * Returns the child storages of this storage.
     */
    @Pure
    public @Nonnull @NonFrozen ReadOnlyList<@Nonnull Storage> getChildStorages() {
        return childStorages;
    }
    
    /**
     * Registers the given child storage at this storage.
     */
    @Impure
    void addChildStorage(@Nonnull Storage childStorage) {
        childStorages.add(childStorage);
    }
    
}
