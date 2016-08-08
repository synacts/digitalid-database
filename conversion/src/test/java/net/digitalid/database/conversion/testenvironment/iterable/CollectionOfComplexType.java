package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

import net.digitalid.database.conversion.testenvironment.AnotherClass;
import net.digitalid.database.conversion.testenvironment.referenced.ReferencedEntity;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class CollectionOfComplexType {
    
    public final @Nonnull FreezableArrayList<@Nonnull ReferencedEntity> anotherClass;
    
    CollectionOfComplexType(@Nonnull FreezableArrayList<@Nonnull ReferencedEntity> anotherClass) {
        this.anotherClass = anotherClass;
    }
    
}
