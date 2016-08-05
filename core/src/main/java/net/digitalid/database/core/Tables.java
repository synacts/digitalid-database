package net.digitalid.database.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;

import net.digitalid.database.storage.Site;

/**
 *
 */
public class Tables {
    
    private final static @Nonnull FreezableHashMap<@Nonnull String, @Nonnull Table> tables = FreezableHashMapBuilder.build();
    
    @Impure
    public static @Nullable Table add(@Nonnull Table table) {
        return tables.put(table.getName(), table);
    }
    
    @Pure
    public static @Nonnull Table get(@Nonnull String qualifiedTableName) {
        return tables.get(qualifiedTableName);
    }
    
}
