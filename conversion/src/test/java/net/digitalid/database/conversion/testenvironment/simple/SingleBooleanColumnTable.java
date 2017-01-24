package net.digitalid.database.conversion.testenvironment.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.generation.Recover;

@GenerateBuilder
@GenerateConverter
public class SingleBooleanColumnTable  {
    
    public final @Nonnull Boolean value;
    
    protected SingleBooleanColumnTable(@Nonnull Boolean value) {
        this.value = value;
    }
    
    @Pure
    @Recover
    public static @Nonnull SingleBooleanColumnTable get(@Nonnull Boolean value) {
        return new SingleBooleanColumnTable(value);
    }
    
}
