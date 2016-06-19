package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.conversion.converter.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

/**
 *
 */
public class ReferencedCollectionClass implements Convertible {
    
    @GenericTypes(Integer.class)
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    public final Integer additionalField;
    
    private ReferencedCollectionClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers, @Nonnull Integer additionalField) {
        this.listOfIntegers = listOfIntegers;
        this.additionalField = additionalField;
    }
    
    @Constructing
    public static @Nonnull ReferencedCollectionClass get(@Nonnull Integer additionalField, @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers) {
        return new ReferencedCollectionClass(listOfIntegers, additionalField);
    }
    
}
