package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.Site;
import net.digitalid.database.core.SiteBuilder;

/**
 * A subject belongs to a {@link Site site} and can have {@link PersistentProperty persistent properties}.
 */
@Immutable
public interface Subject extends RootInterface {
    
    /* -------------------------------------------------- Site -------------------------------------------------- */
    
    /**
     * Stores the default site for subjects.
     */
    public static final @Nonnull Site DEFAULT_SITE = SiteBuilder.withName("default").build();
    
    /**
     * Returns the site of this subject.
     */
    @Pure
    public default @Nonnull Site getSite() {
        return DEFAULT_SITE;
    }
    
}
