package net.digitalid.database.storage;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.equality.Unequal;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This class allows the same code to work on both hosts and clients.
 */
@Mutable
public abstract class Site extends RootClass {
    
    /* -------------------------------------------------- Queries -------------------------------------------------- */
    
    /**
     * Returns whether this site is a host.
     */
    @Pure
    public abstract boolean isHost();
    
    /**
     * Returns whether this site is a client.
     */
    @Pure
    public final boolean isClient() {
        return !isHost();
    }
    
    /* -------------------------------------------------- Database Name -------------------------------------------------- */
    
    /**
     * Returns the database name of this site.
     */
    @Pure
    public abstract @Nonnull @CodeIdentifier @MaxSize(63) @Unequal("general") String getDatabaseName();
    
    /* -------------------------------------------------- Entity Reference -------------------------------------------------- */
        
    /**
     * Returns the foreign key referenced by the entity column on this site.
     * 
     * TODO: The return type probably shouldn't be a string but rather an SQL syntax node?
     */
    @Pure
    public abstract @Nonnull String getEntityReference();
    
}
