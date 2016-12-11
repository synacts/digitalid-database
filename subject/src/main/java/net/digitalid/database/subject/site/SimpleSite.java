package net.digitalid.database.subject.site;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.subject.SubjectModule;
import net.digitalid.database.subject.SubjectModuleBuilder;

/**
 * This class suppresses the generation of the {@link SubjectModule}, which is no problem because it has no persistent properties anyway.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SimpleSite extends RootClass implements Site {
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    static final @Nonnull SubjectModule<SimpleSite> MODULE = SubjectModuleBuilder.withSubjectConverter(SiteConverterBuilder.withSiteClass(SimpleSite.class).build()).build();
    
    @Pure
    @Override
    public @Nonnull SubjectModule<?> module() {
        return MODULE;
    }
    
}
