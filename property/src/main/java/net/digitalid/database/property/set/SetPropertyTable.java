package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Derive;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.PropertyTable;
import net.digitalid.database.property.Subject;

/**
 * The set property table stores the {@link SetPropertyEntry set property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SetPropertyTable<S extends Subject, V, E> extends PropertyTable<S, SetPropertyEntry<S, V>> implements Valid.Value<V> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("SetPropertyEntryConverterBuilder.<S, V, E>withName(getFullNameWithUnderlines()).withPropertyTable(this).build()")
    public abstract @Nonnull SetPropertyEntryConverter<S, V, E> getEntryConverter();
    
    /* -------------------------------------------------- Provided Object Extractor -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object from the subject.
     */
    @Pure
    @Default("object -> null")
    public abstract @Nonnull UnaryFunction<S, E> getProvidedObjectExtractor();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public abstract @Nonnull Converter<V, E> getValueConverter();
    
}
