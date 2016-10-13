package net.digitalid.database.auxiliary;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.generation.Recover;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.type.Embedded;

/**
 * This class is an alternative to {@link Void} to comply with non-nullable parameters and return values.
 */
@Embedded
@Stateless
@GenerateConverter
public final class None {
    
    /**
     * Creates a new none.
     */
    private None() {}
    
    /**
     * Stores the only instance of this class.
     */
    public static final @Nonnull None INSTANCE = new None();
    
    /**
     * Returns the only instance of this class.
     */
    @Pure
    @Recover
    public static @Nonnull None getInstance() {
        return INSTANCE;
    }
    
}
