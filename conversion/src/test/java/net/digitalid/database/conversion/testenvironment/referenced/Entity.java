package net.digitalid.database.conversion.testenvironment.referenced;

import java.sql.Types;

import javax.annotation.Nonnull;


import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

import net.digitalid.database.annotations.metadata.References;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Entity  {
    
    @References(foreignTable = "referenced_table_1", columnName = "id", onDelete = References.Action.CASCADE, columnType = Types.INTEGER)
    public final @Nonnull ReferencedEntity referencedEntity;
    
    Entity(@Nonnull ReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }
    
}
