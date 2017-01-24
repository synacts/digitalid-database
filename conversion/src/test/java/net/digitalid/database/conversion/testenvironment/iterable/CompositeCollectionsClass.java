package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.annotations.type.Embedded;

/**
 *
 */
@GenerateBuilder
// TODO: @GenerateConverter
public class CompositeCollectionsClass  {
    
//    @Embedded
    static class ListOfIntegers extends FreezableArrayList<Integer> {}
    
    @Embedded
    public final @Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers;
    
    CompositeCollectionsClass(@Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers) {
        this.listOfListOfIntegers = listOfListOfIntegers;
    }
    
}
