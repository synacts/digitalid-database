package net.digitalid.database.conversion.testenvironment.embedded;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;

@GenerateBuilder
@GenerateSubclass
@GenerateTableConverter
public interface EmbeddedConvertibles  {
    
    @Pure
    public @Nonnull Convertible1 getConvertible1();
    
    @Pure
    public @Nonnull Convertible2 getConvertible2();
    
}
