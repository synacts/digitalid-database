package net.digitalid.database.conversion.testenvironment.referenced;

import javax.annotation.Nonnull;


import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Entity  {
    
    @References(foreignTable = "referenced_table_1", columnName = "id", onDelete = References.Action.CASCADE, columnType = SQLType.INTEGER32)
    public final @Nonnull ReferencedEntity referencedEntity;
    
    Entity(@Nonnull ReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }
    
}
