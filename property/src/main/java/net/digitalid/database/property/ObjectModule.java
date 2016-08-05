package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.storage.Module;

/**
 * Description.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class ObjectModule<O> extends Module {
    
    /* -------------------------------------------------- Converter -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the object.
     */
    @Pure
    public abstract @Nonnull Converter<O, Void> getConverter();
    
}
