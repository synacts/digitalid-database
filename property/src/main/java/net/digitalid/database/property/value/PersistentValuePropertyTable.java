package net.digitalid.database.property.value;

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

import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.property.Subject;

/**
 * The value property table stores the {@link PersistentValuePropertyEntry value property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentValuePropertyTable<S extends Subject, V, E> extends PersistentPropertyTable<S, PersistentValuePropertyEntry<S, V>> implements Valid.Value<V> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("PersistentValuePropertyEntryConverterBuilder.<S, V, E>withName(getFullNameWithUnderlines()).withPropertyTable(this).build()")
    public abstract @Nonnull PersistentValuePropertyEntryConverter<S, V, E> getEntryConverter();
    
    /* -------------------------------------------------- Provided Object Extractor -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object from the subject.
     */
    @Pure
    @Default("subject -> null")
    public abstract @Nonnull UnaryFunction<@Nonnull S, E> getProvidedObjectExtractor();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the value of the property.
     */
    @Pure
    public abstract @Nonnull Converter<V, E> getValueConverter();
    
    /* -------------------------------------------------- Default Value -------------------------------------------------- */
    
    /**
     * Returns the default value of the property.
     */
    @Pure
    public abstract @Valid V getDefaultValue();
    
}
