package net.digitalid.database.conversion.testenvironment.referenced;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.enumerations.ForeignKeyAction;
import net.digitalid.database.enumerations.SQLType;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Entity  {
    
    public final @Nonnull ReferencedEntity referencedEntity;
    
    Entity(@Nonnull ReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }
    
}
