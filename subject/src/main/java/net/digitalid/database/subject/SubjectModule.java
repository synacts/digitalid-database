package net.digitalid.database.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.storage.Module;
import net.digitalid.database.subject.site.Site;

/**
 * A subject module contains the tables of all properties in the subject's class.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SubjectModule<SITE extends Site<?>, SUBJECT extends Subject<SITE>> extends Module {
    
    /* -------------------------------------------------- Subject Converter -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the subject.
     */
    @Pure
    public abstract @Nonnull Converter<SUBJECT, @Nonnull SITE> getSubjectConverter();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName() {
        return getSubjectConverter().getTypeName();
    }
    
}
