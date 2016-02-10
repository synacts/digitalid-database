package net.digitalid.database.conversion.testenvironment.iterable;


import javax.annotation.Nonnull;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.dialect.annotations.Embedd;

/**
 *
 */
public class SimpleCollectionsClass implements Convertible {
    
    @Embedd
    @GenericTypes(Integer.class)
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    private SimpleCollectionsClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers) {
        this.listOfIntegers = listOfIntegers;
    }
    
    @Constructing
    public static @Nonnull SimpleCollectionsClass get(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers) {
        return new SimpleCollectionsClass(listOfIntegers);
    }
    
}
