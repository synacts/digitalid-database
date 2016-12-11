package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Normalize;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This type models a node in the storage tree.
 * 
 * @see Module
 * @see Table
 */
@Mutable
public interface Storage extends RootInterface {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    /**
     * Registers this storage at the given parent module.
     * <p>
     * <em>Important:</em> This method should never be called manually.
     */
    @Pure
    public default @Nullable Module registerAt(@Nullable Module parentModule) {
        if (parentModule != null) { parentModule.addChildStorage(this); }
        return parentModule;
    }
    
    /**
     * Returns the parent module to which this storage belongs.
     */
    @Pure
    @Default("null") // TODO: Is this necessary?
    @Normalize("registerAt(parentModule)")
    public @Nullable Module getParentModule();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this storage.
     */
    @Pure
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName();
    
    /**
     * Returns the full name of this storage with the given delimiter between the names of the parent modules in the given direction.
     */
    @Pure
    public default @Nonnull String getFullName(@Nonnull String delimiter, boolean rootToLeaf) {
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
    // TODO: @Cached
    public default @Nonnull String getFullNameWithPeriods() {
        return getFullName(".", false);
    }
    
    /**
     * Returns the full name of this storage with underlines between the names of the parent modules from root to leaf.
     */
    @Pure
    // TODO: @Cached
    public default @Nonnull @CodeIdentifier String getFullNameWithUnderlines() {
        return getFullName("_", true);
    }
    
}
