package net.digitalid.database.conversion.testenvironment.columnconstraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.validation.annotations.math.MultipleOf;

/**
 *
 */
public class ConstraintIntegerColumnTable implements Convertible {
    
    @MultipleOf(7)
    public final @Nonnull Integer value;
    
    protected ConstraintIntegerColumnTable(@Nonnull Integer value) {
        this.value = value;
    }
    
    @Constructing
    public static @Nonnull ConstraintIntegerColumnTable get(@Nonnull Integer value) {
        return new ConstraintIntegerColumnTable(value);
    }
    
}
