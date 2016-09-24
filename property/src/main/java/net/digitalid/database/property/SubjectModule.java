package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.storage.Module;

/**
 * A subject module contains the tables of all properties in the subject's class.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class SubjectModule<S extends Subject> extends Module {
    
    /* -------------------------------------------------- Subject Converter -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the subject.
     */
    @Pure
    public abstract @Nonnull Converter<S, Void> getSubjectConverter();
    
}
