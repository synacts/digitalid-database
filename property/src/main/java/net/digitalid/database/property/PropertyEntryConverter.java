package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.simple.SimplePropertyEntryConverter;

/**
 * This class converts the {@link PropertyEntry entries} of the {@link PropertyTable}.
 * 
 * @see SimplePropertyEntryConverter
 */
@Immutable
public abstract class PropertyEntryConverter<O, V, E extends PropertyEntry<O, V>> extends RootClass implements Converter<E, Void> {
    
    /* -------------------------------------------------- Object Converter -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the object.
     */
    @Pure
    public abstract @Nonnull Converter<O, Void> getObjectConverter();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the value of the property.
     */
    @Pure
    @TODO(task = "Support externally provided recover information, maybe with another generic type and a function that can extract it from the object.", date = "2016-08-30", author = Author.KASPAR_ETTER)
    public abstract @Nonnull Converter<V, Void> getValueConverter();
    
}
