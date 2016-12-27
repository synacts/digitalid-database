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
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.site.Site;

/**
 * The set property table stores the {@link PersistentSetPropertyEntry set property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface PersistentSetPropertyTable<SITE extends Site<?>, SUBJECT extends Subject<SITE>, VALUE, PROVIDED_FOR_VALUE> extends PersistentPropertyTable<SITE, SUBJECT, PersistentSetPropertyEntry<SUBJECT, VALUE>>, Valid.Value<VALUE> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("PersistentSetPropertyEntryConverterBuilder.withPropertyTable(this).build()")
    public @Nonnull PersistentSetPropertyEntryConverter<SITE, SUBJECT, VALUE, PROVIDED_FOR_VALUE> getEntryConverter();
    
    /* -------------------------------------------------- Provided Object Extractor -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object from the subject.
     */
    @Pure
    @Default("subject -> null")
    public @Nonnull UnaryFunction<@Nonnull SUBJECT, PROVIDED_FOR_VALUE> getProvidedObjectExtractor();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> getValueConverter();
    
}
