package net.digitalid.database.core;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.ReadOnlyMap;

import net.digitalid.database.storage.Site;

/**
 *
 */
public interface Table {
    
    @Pure
    public @Nonnull String getName(@Nonnull Site site);
    
    @Pure
    public @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull ? extends Table> getForeignKeys();
    
    @Pure
    public int getTypeOfColumn(@Nonnull String value);
    
    @Pure
    public int getNumberOfColumnsForField(@Nonnull String fieldName);
    
}
