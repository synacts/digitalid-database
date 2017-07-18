package net.digitalid.database.conversion.testenvironment.columnconstraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Recover;

@GenerateBuilder
@GenerateTableConverter
public class BooleanColumnDefaultTrueTable  {
    
    @Default("true")
    public final @Nonnull Boolean value;
    
    protected BooleanColumnDefaultTrueTable(@Nonnull Boolean value) {
        this.value = value;
    }
    
    @Pure
    @Recover
    public static @Nonnull BooleanColumnDefaultTrueTable get(@Nonnull Boolean value) {
        return new BooleanColumnDefaultTrueTable(value);
    }
    
}
