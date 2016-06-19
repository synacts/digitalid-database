package net.digitalid.database.core.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

/**
 * This class provides an interface so that the same code works on both hosts and clients.
 */
public abstract class Site {
    
    /* -------------------------------------------------- Prefix -------------------------------------------------- */
    
    // TODO: should be checked otherwise using an Annotation.
//    /**
//     * Returns whether the given prefix is valid.
//     * 
//     * @param prefix the prefix to be checked.
//     * 
//     * @return whether the given prefix is valid.
//     */
//    @Pure
//    public static boolean isValidPrefix(@Nonnull String prefix) {
//        return prefix.length() <= 40 && Database.getConfiguration().isValidIdentifier(prefix);
//    }
    
    /**
     * Stores the prefix of the site-specific database tables.
     */
    private final @Nonnull String prefix;
    
    /**
     * Returns the prefix of the site-specific database tables.
     * 
     * @return the prefix of the site-specific database tables.
     */
    @Pure
    @Override
    public final @Nonnull String toString() {
        return prefix;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new site with the given prefix.
     * 
     * @param prefix the prefix of the site-specific database tables.
     */
    protected Site(@Nonnull String prefix) {
        this.prefix = prefix;
    }
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
        
    /**
     * Returns the foreign key referenced by the entity column.
     * 
     * @return the foreign key referenced by the entity column.
     */
    @Pure
    public abstract @Nonnull String getEntityReference();
    
}
