package net.digitalid.database.conversion.testenvironment.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.generation.Recover;

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
    @Recover
    public static @Nonnull MultiBooleanColumnTable get(@Nonnull Boolean firstValue, @Nonnull Boolean secondValue) {
        return new MultiBooleanColumnTable(firstValue, secondValue);
    }
    
}
