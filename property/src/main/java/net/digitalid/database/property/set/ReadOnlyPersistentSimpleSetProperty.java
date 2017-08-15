package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.subject.Subject;

/**
 * This interface simplifies the declaration of {@link ReadOnlyPersistentSetProperty}.
 * 
 * @see WritablePersistentSimpleSetPropertyImplementation
 */
@ThreadSafe
@ReadOnly(WritablePersistentSimpleSetPropertyImplementation.class)
public interface ReadOnlyPersistentSimpleSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE> extends ReadOnlyPersistentSetProperty<SUBJECT, VALUE, ReadOnlySet<@Nonnull @Valid VALUE>> {}
