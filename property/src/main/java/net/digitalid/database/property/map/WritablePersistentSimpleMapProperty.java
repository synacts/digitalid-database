package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.FreezableMap;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.subject.Subject;

/**
 * This class simplifies the creation and declaration of {@link WritablePersistentMapProperty}.
 * 
 * @see WritablePersistentSimpleMapPropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentSimpleMapProperty.class)
public interface WritablePersistentSimpleMapProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable KEY, @Unspecifiable VALUE> extends WritablePersistentMapProperty<SUBJECT, KEY, VALUE, ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>, FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>>, ReadOnlyPersistentSimpleMapProperty<SUBJECT, KEY, VALUE> {}
