package net.digitalid.database.conversion.testenvironment.referenced;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.converter.Convertible;

import net.digitalid.database.dialect.annotations.References;

/**
 *
 */
public class Entity implements Convertible {
    
    @References(foreignTable = "referenced_table_1", columnName = "id", onDelete = References.Action.CASCADE)
    public final @Nonnull ReferencedEntity referencedEntity;
    
    private Entity(@Nonnull ReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }
    
    public static @Nonnull Entity get(@Nonnull ReferencedEntity referencedEntity) {
        return new Entity(referencedEntity);
    }
    
}
