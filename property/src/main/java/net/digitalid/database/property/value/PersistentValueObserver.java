package net.digitalid.database.property.value;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.property.Property;
import net.digitalid.utility.property.value.ValueObserver;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * Objects that implement this interface can be used to {@link Property#register(net.digitalid.utility.property.Observer) observe} {@link ReadOnlyPersistentValueProperty persistent value properties}.
 */
@Mutable
@Functional
public interface PersistentValueObserver<@Unspecifiable SUBJECT extends Subject<?>, @Specifiable VALUE> extends ValueObserver<VALUE, DatabaseException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>> {}
