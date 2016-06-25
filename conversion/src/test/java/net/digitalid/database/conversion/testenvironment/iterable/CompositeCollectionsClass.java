package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.annotations.GenericTypes;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.dialect.annotations.Embedd;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class CompositeCollectionsClass  {
    
    @Embedd
    static class ListOfIntegers extends FreezableArrayList<Integer> {}
    
    @Embedd
    public final @Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers;
    
    CompositeCollectionsClass(@Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers) {
        this.listOfListOfIntegers = listOfListOfIntegers;
    }
    
}
