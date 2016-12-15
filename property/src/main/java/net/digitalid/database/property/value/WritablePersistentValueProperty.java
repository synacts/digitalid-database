package net.digitalid.database.property.value;

import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.property.value.WritableValueProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * This writable property stores a value in the persistent database.
 * 
 * @see WritablePersistentValuePropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentValueProperty.class)
public interface WritablePersistentValueProperty<SUBJECT extends Subject<?>, VALUE> extends WritableValueProperty<VALUE, DatabaseException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE> {}
