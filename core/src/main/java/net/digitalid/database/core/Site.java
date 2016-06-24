package net.digitalid.database.core;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This interface allows the same code to work on both hosts and clients.
 */
@Mutable
public interface Site extends RootInterface {
    
    /* -------------------------------------------------- Database Name -------------------------------------------------- */
    
    /**
     * Returns the database name of this site.
     */
    @Pure
    public @Nonnull @MaxSize(64) @CodeIdentifier String getDatabaseName();
    
    /* -------------------------------------------------- Entity Reference -------------------------------------------------- */
        
    /**
     * Returns the foreign key referenced by the entity column on this site.
     */
    @Pure
    public @Nonnull String getEntityReference();
    
}
