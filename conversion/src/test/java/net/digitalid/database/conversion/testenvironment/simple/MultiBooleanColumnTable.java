package net.digitalid.database.conversion.testenvironment.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class MultiBooleanColumnTable  {
    
    public final @Nonnull Boolean firstValue;
    public final @Nonnull Boolean secondValue;
    
    protected MultiBooleanColumnTable(@Nonnull Boolean firstValue, @Nonnull Boolean secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }
    
    @Pure
    @Constructing
    public static @Nonnull MultiBooleanColumnTable get(@Nonnull Boolean firstValue, @Nonnull Boolean secondValue) {
        return new MultiBooleanColumnTable(firstValue, secondValue);
    }
    
}
