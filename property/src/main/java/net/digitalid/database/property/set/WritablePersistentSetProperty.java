/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.FreezableSet;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.property.set.WritableSetProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.subject.Subject;

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
public interface WritablePersistentSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE, @Unspecifiable READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>, @Unspecifiable FREEZABLE_SET extends FreezableSet<@Nonnull @Valid VALUE>> extends WritableSetProperty<VALUE, READONLY_SET, DatabaseException, RecoveryException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET> {}
