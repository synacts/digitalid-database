package net.digitalid.database.unit;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This interface models a database unit.
 * A unit is modeled as a database schema.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface Unit {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this unit.
     */
    @Pure
    public abstract @Nonnull @CodeIdentifier @MaxSize(63) String getName();
    
    /* -------------------------------------------------- Default -------------------------------------------------- */
    
    /**
     * Stores a default instance of a database unit.
     */
    public static final @Nonnull Unit DEFAULT = UnitBuilder.withName("default").build();
    
}
