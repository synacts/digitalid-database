package net.digitalid.database.property.value;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.property.value.WritableValueProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.subject.Subject;

/**
 * This writable property stores a value in the persistent database.
 * 
 * @see WritablePersistentValuePropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentValueProperty.class)
public interface WritablePersistentValueProperty<@Unspecifiable SUBJECT extends Subject<?>, @Specifiable VALUE> extends WritableValueProperty<VALUE, DatabaseException, RecoveryException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE> {}
