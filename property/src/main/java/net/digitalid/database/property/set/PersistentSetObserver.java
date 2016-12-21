package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.property.Property;
import net.digitalid.utility.property.set.SetObserver;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * Objects that implement this interface can be used to {@link Property#register(net.digitalid.utility.property.Observer) observe} {@link ReadOnlyPersistentSetProperty persistent set properties}.
 */
@Mutable
@Functional
public interface PersistentSetObserver<SUBJECT extends Subject<?>, VALUE, READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>> extends SetObserver<VALUE, READONLY_SET, DatabaseException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>> {}
