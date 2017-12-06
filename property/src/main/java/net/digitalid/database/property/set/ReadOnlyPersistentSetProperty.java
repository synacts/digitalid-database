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
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.property.set.ReadOnlySetProperty;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.property.subject.Subject;

/**
 * This read-only property stores a set of values in the persistent database.
 * 
 * @see WritablePersistentSetProperty
 * @see ReadOnlyPersistentSimpleSetProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentSetProperty.class)
public interface ReadOnlyPersistentSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE, @Unspecifiable READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>> extends ReadOnlySetProperty<VALUE, READONLY_SET, DatabaseException, RecoveryException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>>, PersistentProperty<SUBJECT, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>> {
    
    /* -------------------------------------------------- Getter -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @Nonnull @NonFrozen @NonNullableElements READONLY_SET get() throws DatabaseException, RecoveryException;
    
}
