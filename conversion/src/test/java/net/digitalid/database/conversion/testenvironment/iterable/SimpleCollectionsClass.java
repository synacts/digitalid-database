package net.digitalid.database.conversion.testenvironment.iterable;


import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.annotations.metadata.Embedded;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class SimpleCollectionsClass  {
    
    @Embedded
    @GenericTypes(Integer.class)
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    SimpleCollectionsClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers) {
        this.listOfIntegers = listOfIntegers;
    }
    
}
