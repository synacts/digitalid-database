package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Derive;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.PropertyTable;
import net.digitalid.database.property.Subject;

/**
 * Description.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class ValuePropertyTable<S extends Subject, V> extends PropertyTable<S, ValuePropertyEntry<S, V>> implements Valid.Value<V> {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    @Derive("ValuePropertyEntryConverterBuilder.<S, V>withName(getFullNameWithUnderlines()).withPropertyTable(this).build()")
    public abstract @Nonnull ValuePropertyEntryConverter<S, V> getEntryConverter();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the value of the property.
     */
    @Pure
    @TODO(task = "Support externally provided recover information, maybe with another generic type and a function that can extract it from the object.", date = "2016-08-30", author = Author.KASPAR_ETTER)
    public abstract @Nonnull Converter<V, Void> getValueConverter();
    
}
