package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * This interface models a database unit.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public interface Site extends Subject {
    
    /* -------------------------------------------------- Schema Name -------------------------------------------------- */
    
    /**
     * Returns the schema name of this site.
     */
    @Pure
    public abstract @Nonnull @CodeIdentifier @MaxSize(63) String getSchemaName();
    
    /* -------------------------------------------------- Queries -------------------------------------------------- */
    
    /**
     * Returns whether this site is a host.
     */
    @Pure
    public default boolean isHost() {
        return false;
    }
    
    /**
     * Returns whether this site is a client.
     */
    @Pure
    public default boolean isClient() {
        return !isHost();
    }
    
    /* -------------------------------------------------- Subject -------------------------------------------------- */
    
    @Pure
    @Override
    public default @Nonnull Site getSite() {
        return this;
    }
    
}
