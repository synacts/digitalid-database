package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.interfaces.Subject;

/**
 * This interface simplifies the declaration of {@link ReadOnlyPersistentMapProperty}.
 * 
 * @see WritablePersistentSimpleMapProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentSimpleMapProperty.class)
public interface ReadOnlyPersistentSimpleMapProperty<S extends Subject, K, V> extends ReadOnlyPersistentMapProperty<S, K, V, ReadOnlyMap<@Nonnull @Valid("key") K, @Nonnull @Valid V>> {}
