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
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.property.Property;
import net.digitalid.utility.property.set.SetObserver;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.subject.Subject;

/**
 * Objects that implement this interface can be used to {@link Property#register(net.digitalid.utility.property.Observer) observe} {@link ReadOnlyPersistentSetProperty persistent set properties}.
 */
@Mutable
@Functional
public interface PersistentSetObserver<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE, @Unspecifiable READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>> extends SetObserver<VALUE, READONLY_SET, DatabaseException, RecoveryException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>> {}
