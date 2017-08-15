package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.FreezableSet;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.subject.Subject;

/**
 * This type simplifies the creation and declaration of {@link WritablePersistentSetProperty}.
 * 
 * @see WritablePersistentSimpleSetPropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentSimpleSetProperty.class)
public interface WritablePersistentSimpleSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE> extends WritablePersistentSetProperty<SUBJECT, VALUE, ReadOnlySet<@Nonnull @Valid VALUE>, FreezableSet<@Nonnull @Valid VALUE>>, ReadOnlyPersistentSimpleSetProperty<SUBJECT, VALUE> {}
