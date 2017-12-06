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
