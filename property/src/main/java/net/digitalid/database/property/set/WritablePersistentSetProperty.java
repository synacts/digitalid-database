package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.FreezableSet;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.property.set.WritableSetProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * This writable property stores a set of values in the persistent database.
 * 
 * <em>Important:</em> Make sure that {@code FREEZABLE_SET} is a sub-type of {@code READONLY_SET}!
 * Unfortunately, this cannot be enforced with the limited Java generics.
 * 
 * @invariant !get().containsNull() : "None of the values may be null.";
 * @invariant get().matchAll(getValidator()) : "Each value has to be valid.";
 * 
 * @see WritablePersistentSimpleSetProperty
 * @see WritablePersistentSetPropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentSetProperty.class)
public interface WritablePersistentSetProperty<SUBJECT extends Subject<?>, VALUE, READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>, FREEZABLE_SET extends FreezableSet<@Nonnull @Valid VALUE>> extends WritableSetProperty<VALUE, READONLY_SET, DatabaseException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET> {}
