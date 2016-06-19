package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.conversion.converter.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.dialect.annotations.Embedd;

/**
 *
 */
public class CollectionAndAdditionalFieldClass implements Convertible {
    
    @Embedd
    @GenericTypes(Integer.class)
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    public final Integer additionalField;
    
    private CollectionAndAdditionalFieldClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers, @Nonnull Integer additionalField) {
        this.listOfIntegers = listOfIntegers;
        this.additionalField = additionalField;
    }
    
    @Constructing
    public static @Nonnull CollectionAndAdditionalFieldClass get(@Nonnull Integer additionalField, @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers) {
        return new CollectionAndAdditionalFieldClass(listOfIntegers, additionalField);
    }
    
}
