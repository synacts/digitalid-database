package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.ReadOnlyMap;


/**
 *
 */
public interface Table {
    
    @Pure
    public @Nonnull String getName();
    
    @Pure
    public @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull ? extends Table> getForeignKeys();
    
    @Pure
    public int getTypeOfColumn(@Nonnull String value);
    
    @Pure
    public int getNumberOfColumnsForField(@Nonnull String fieldName);
    
}
