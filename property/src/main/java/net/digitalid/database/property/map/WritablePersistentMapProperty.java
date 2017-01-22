package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.FreezableMap;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.property.map.WritableMapProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * This writable property stores a map of key-value pairs in the persistent database.
 * 
 * <em>Important:</em> Make sure that {@code FREEZABLE_MAP} is a sub-type of {@code READONLY_MAP}!
 * Unfortunately, this cannot be enforced with the limited Java generics.
 * 
 * @invariant !get().keySet().containsNull() : "None of the keys may be null.";
 * @invariant !get().values().containsNull() : "None of the values may be null.";
 * @invariant get().keySet().matchAll(getKeyValidator()) : "Each key has to be valid.";
 * @invariant get().values().matchAll(getValueValidator()) : "Each value has to be valid.";
 * 
 * @see WritablePersistentMapPropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentMapProperty.class)
public interface WritablePersistentMapProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable KEY, @Unspecifiable VALUE, @Unspecifiable READONLY_MAP extends ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>, @Unspecifiable FREEZABLE_MAP extends FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> extends WritableMapProperty<KEY, VALUE, READONLY_MAP, DatabaseException, PersistentMapObserver<SUBJECT, KEY, VALUE, READONLY_MAP>, ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP>>, ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP> {}
