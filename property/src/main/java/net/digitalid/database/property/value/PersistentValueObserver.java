package net.digitalid.database.property.value;

import net.digitalid.utility.property.value.ValueObserver;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * Objects that implement this interface can be used to {@link #register(net.digitalid.utility.property.Observer) observe} {@link ReadOnlyPersistentValueProperty persistent value properties}.
 */
@Mutable
@Functional
public interface PersistentValueObserver<SUBJECT extends Subject<?>, VALUE> extends ValueObserver<VALUE, DatabaseException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>> {}
