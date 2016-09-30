package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.Subject;

/**
 * This interface simplifies the declaration of {@link ReadOnlyPersistentSetProperty}.
 * 
 * @see WritablePersistentSimpleSetProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentSimpleSetProperty.class)
public interface ReadOnlyPersistentSimpleSetProperty<S extends Subject, V> extends ReadOnlyPersistentSetProperty<S, V, ReadOnlySet<@Nonnull @Valid V>> {}
