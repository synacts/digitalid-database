package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.functional.interfaces.BinaryFunction;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Derive;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.unit.Unit;

/**
 * The map property table stores the {@link PersistentMapPropertyEntry map property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface PersistentMapPropertyTable<SITE extends Unit<?>, SUBJECT extends Subject<SITE>, KEY, VALUE, PROVIDED_FOR_KEY, PROVIDED_FOR_VALUE> extends PersistentPropertyTable<SITE, SUBJECT, PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>>, Valid.Key<KEY>, Valid.Value<VALUE> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("PersistentMapPropertyEntryConverterBuilder.withPropertyTable(this).build()")
    public @Nonnull PersistentMapPropertyEntryConverter<SITE, SUBJECT, KEY, VALUE, PROVIDED_FOR_KEY, PROVIDED_FOR_VALUE> getEntryConverter();
    
    /* -------------------------------------------------- Extractors -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object for the key from the subject.
     */
    @Pure
    @Default("subject -> null")
    public @Nonnull UnaryFunction<@Nonnull SUBJECT, PROVIDED_FOR_KEY> getProvidedObjectForKeyExtractor();
    
    /**
     * Returns the function that extracts the externally provided object for the value from the subject and the key.
     */
    @Pure
    @Default("(subject, key) -> null")
    public @Nonnull BinaryFunction<@Nonnull SUBJECT, @Nonnull KEY, PROVIDED_FOR_VALUE> getProvidedObjectForValueExtractor();
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the keys of the property.
     */
    @Pure
    public @Nonnull Converter<KEY, PROVIDED_FOR_KEY> getKeyConverter();
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> getValueConverter();
    
}
