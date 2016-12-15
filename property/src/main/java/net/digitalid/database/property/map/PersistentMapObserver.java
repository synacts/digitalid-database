package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.property.map.MapObserver;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * Objects that implement this interface can be used to {@link #register(net.digitalid.utility.property.Observer) observe} {@link ReadOnlyPersistentMapProperty persistent map properties}.
 */
@Mutable
@Functional
public interface PersistentMapObserver<SUBJECT extends Subject<?>, KEY, VALUE, READONLY_MAP extends ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> extends MapObserver<KEY, VALUE, READONLY_MAP, DatabaseException, PersistentMapObserver<SUBJECT, KEY, VALUE, READONLY_MAP>, ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP>> {}
