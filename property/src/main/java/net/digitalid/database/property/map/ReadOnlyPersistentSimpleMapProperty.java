package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.subject.Subject;

/**
 * This interface simplifies the declaration of {@link ReadOnlyPersistentMapProperty}.
 * 
 * @see WritablePersistentSimpleMapPropertyImplementation
 */
@ThreadSafe
@ReadOnly(WritablePersistentSimpleMapPropertyImplementation.class)
public interface ReadOnlyPersistentSimpleMapProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable KEY, @Unspecifiable VALUE> extends ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> {}
