package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Derive;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This class models a node in the storage tree.
 * 
 * @see Module
 * @see Table
 */
@Mutable
public abstract class Storage extends RootClass {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    /**
     * Returns the parent module to which this storage belongs.
     */
    @Pure
    @Default("null") // TODO: Is this necessary?
    public abstract @Nullable Module getParentModule();
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    @Pure
    @Override
    @CallSuper
    protected void initialize() {
        final @Nullable Module module = getParentModule();
        if (module != null) { module.addChildStorage(this); }
        super.initialize();
    }
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this storage.
     */
    @Pure
    public abstract @Nonnull @CodeIdentifier @MaxSize(63) String getName();
    
    /**
     * Returns the full name of this storage with the given delimiter between the names of the parent modules in the given direction.
     */
    @Pure
    public @Nonnull String getFullName(@Nonnull String delimiter, boolean rootToLeaf) {
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
    public abstract @Nonnull String getFullNameWithPeriods();
    
    /**
     * Returns the full name of this storage with underlines between the names of the parent modules from root to leaf.
     */
    @Pure
    @Derive("getFullName(\"_\", true)")
    public abstract @Nonnull @CodeIdentifier String getFullNameWithUnderlines();
    
}
