package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.functional.interfaces.BinaryFunction;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Derive;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.interfaces.Subject;

/**
 * The map property table stores the {@link PersistentMapPropertyEntry map property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface PersistentMapPropertyTable<S extends Subject, K, V, EK, EV> extends PersistentPropertyTable<S, PersistentMapPropertyEntry<S, K, V>>, Valid.Key<K>, Valid.Value<V> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("PersistentMapPropertyEntryConverterBuilder.<S, K, V, EK, EV>withName(getFullNameWithUnderlines()).withPropertyTable(this).build()")
    public @Nonnull PersistentMapPropertyEntryConverter<S, K, V, EK, EV> getEntryConverter();
    
    /* -------------------------------------------------- Extractors -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object for the key from the subject.
     */
    @Pure
    @Default("subject -> null")
    public @Nonnull UnaryFunction<@Nonnull S, EK> getProvidedObjectForKeyExtractor();
    
    /**
     * Returns the function that extracts the externally provided object for the value from the subject and the key.
     */
    @Pure
    @Default("(subject, key) -> null")
    public @Nonnull BinaryFunction<@Nonnull S, @Nonnull K, EV> getProvidedObjectForValueExtractor();
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the keys of the property.
     */
    @Pure
    public @Nonnull Converter<K, EK> getKeyConverter();
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public @Nonnull Converter<V, EV> getValueConverter();
    
}
