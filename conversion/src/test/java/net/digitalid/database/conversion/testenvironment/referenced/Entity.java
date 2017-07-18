package net.digitalid.database.conversion.testenvironment.referenced;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;

@GenerateBuilder
@GenerateTableConverter
public class Entity  {
    
    public final @Nonnull ReferencedEntity referencedEntity;
    
    Entity(@Nonnull ReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }
    
}
