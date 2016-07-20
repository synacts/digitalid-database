package net.digitalid.database.conversion.testenvironment.columnconstraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.annotations.Constructing;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class ConstraintIntegerColumnTable  {
    
    @MultipleOf(7)
    public final @Nonnull Integer value;
    
    protected ConstraintIntegerColumnTable(@Nonnull Integer value) {
        this.value = value;
    }
    
    @Pure
    @Constructing
    public static @Nonnull ConstraintIntegerColumnTable get(@Nonnull Integer value) {
        return new ConstraintIntegerColumnTable(value);
    }
    
}
