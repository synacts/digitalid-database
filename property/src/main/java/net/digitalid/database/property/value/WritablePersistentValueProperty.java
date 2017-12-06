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
