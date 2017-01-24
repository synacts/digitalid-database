package net.digitalid.database.conversion.testenvironment.iterable;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class ReferencedCollectionClass  {
    
    public final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers;
    
    public final Integer additionalField;
    
    ReferencedCollectionClass(@Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers, @Nonnull Integer additionalField) {
        this.listOfIntegers = listOfIntegers;
        this.additionalField = additionalField;
    }
    
}
