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
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.subject.Subject;

/**
 * This type simplifies the creation and declaration of {@link WritablePersistentSetProperty}.
 * 
 * @see WritablePersistentSimpleSetPropertyImplementation
 */
@ThreadSafe
@Mutable(ReadOnlyPersistentSimpleSetProperty.class)
public interface WritablePersistentSimpleSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE> extends WritablePersistentSetProperty<SUBJECT, VALUE, ReadOnlySet<@Nonnull @Valid VALUE>, FreezableSet<@Nonnull @Valid VALUE>>, ReadOnlyPersistentSimpleSetProperty<SUBJECT, VALUE> {}
