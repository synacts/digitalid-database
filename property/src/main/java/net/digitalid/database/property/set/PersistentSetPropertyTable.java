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

import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.property.Subject;

/**
 * The set property table stores the {@link PersistentSetPropertyEntry set property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentSetPropertyTable<S extends Subject, V, E> extends PersistentPropertyTable<S, PersistentSetPropertyEntry<S, V>> implements Valid.Value<V> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("PersistentSetPropertyEntryConverterBuilder.<S, V, E>withName(getFullNameWithUnderlines()).withPropertyTable(this).build()")
    public abstract @Nonnull PersistentSetPropertyEntryConverter<S, V, E> getEntryConverter();
    
    /* -------------------------------------------------- Provided Object Extractor -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object from the subject.
     */
    @Pure
    @Default("subject -> null")
    public abstract @Nonnull UnaryFunction<@Nonnull S, E> getProvidedObjectExtractor();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public abstract @Nonnull Converter<V, E> getValueConverter();
    
}
