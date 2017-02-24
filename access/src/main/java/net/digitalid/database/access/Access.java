package net.digitalid.database.access;

import javax.annotation.Nonnull;

import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.validation.annotations.type.Utility;

/**
 * This type stores the configuration of the database access mode.
 */
@Utility
public abstract class Access {
    
    /* -------------------------------------------------- Mode -------------------------------------------------- */
    
    /**
     * Stores the access mode of the database.
     */
    public static final @Nonnull Configuration<Mode> mode = Configuration.with(Mode.SINGLE);
    
}
