package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

/**
 *
 */
public class CollectionAndAdditionalFieldClass implements Convertible {
    
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    public final Integer additionalField;
    
    private CollectionAndAdditionalFieldClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers, @Nonnull Integer additionalField) {
        this.listOfIntegers = listOfIntegers;
        this.additionalField = additionalField;
    }
    
    @Constructing
    public static @Nonnull CollectionAndAdditionalFieldClass get(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers, @Nonnull Integer additionalField) {
        return new CollectionAndAdditionalFieldClass(listOfIntegers, additionalField);
    }
    
}
