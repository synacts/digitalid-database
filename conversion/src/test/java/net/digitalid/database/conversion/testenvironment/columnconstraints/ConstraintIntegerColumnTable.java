package net.digitalid.database.conversion.testenvironment.columnconstraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;
import net.digitalid.utility.validation.annotations.generation.Recover;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;

@GenerateBuilder
@GenerateTableConverter
public class ConstraintIntegerColumnTable  {
    
    @MultipleOf(7)
    public final @Nonnull Integer value;
    
    protected ConstraintIntegerColumnTable(@Nonnull Integer value) {
        this.value = value;
    }
    
    @Pure
    @Recover
    public static @Nonnull ConstraintIntegerColumnTable get(@Nonnull Integer value) {
        return new ConstraintIntegerColumnTable(value);
    }
    
}
