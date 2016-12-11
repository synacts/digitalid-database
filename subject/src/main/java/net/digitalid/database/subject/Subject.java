package net.digitalid.database.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Provided;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.subject.annotations.GenerateSubjectModule;
import net.digitalid.database.subject.site.SimpleSiteBuilder;
import net.digitalid.database.subject.site.Site;

/**
 * A subject belongs to a {@link Site site} and can have persistent properties.
 */
@Immutable
public interface Subject extends RootInterface {
    
    /* -------------------------------------------------- Site -------------------------------------------------- */
    
    /**
     * Stores the default site for subjects.
     */
    public static final @Nonnull Site DEFAULT_SITE = SimpleSiteBuilder.withSchemaName("default").build();
    
    /**
     * Returns the site of this subject.
     */
    @Pure
    @Provided
    @Default("net.digitalid.database.subject.Subject.DEFAULT_SITE")
    public @Nonnull Site getSite();
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    /**
     * Generates and returns the {@link SubjectModule} required to store persistent properties.
     */
    @Pure
    @GenerateSubjectModule
    @TODO(task = "Make it possible that this method can be called getModule() without generating a corresponding field.", date = "2016-12-11", author = Author.KASPAR_ETTER)
    public @Nonnull SubjectModule<?> module();
    
}
