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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.property.value.ReadOnlyValueProperty;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.lock.LockNotHeldByCurrentThread;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.property.subject.Subject;

/**
 * This read-only property stores a value in the persistent database.
 * 
 * @see WritablePersistentValueProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentValueProperty.class)
public interface ReadOnlyPersistentValueProperty<@Unspecifiable SUBJECT extends Subject<?>, @Specifiable VALUE> extends ReadOnlyValueProperty<VALUE, DatabaseException, RecoveryException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>>, PersistentProperty<SUBJECT, PersistentValueObserver<SUBJECT, VALUE>> {
    
    /* -------------------------------------------------- Getter -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Valid VALUE get() throws DatabaseException, RecoveryException;
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    /**
     * Returns the time of the last modification or null if the value has never been set.
     * If it is important that you get the time when the retrieved value has been set, call {@link #getValueWithTimeOfLastModification()} instead.
     */
    @Pure
    @NonCommitting
    public @Nullable Time getTime() throws DatabaseException, RecoveryException;
    
    /* -------------------------------------------------- Combination -------------------------------------------------- */
    
    /**
     * Returns the value of this property with the time of its last modification or null if it has never been set.
     * Contrary to calling {@link #get()} and {@link #getTime()} separately, this method guarantees that the value and time belong together.
     */
    @Pure
    @NonCommitting
    @LockNotHeldByCurrentThread
    public @Nonnull Pair<@Valid VALUE, @Nullable Time> getValueWithTimeOfLastModification() throws DatabaseException, RecoveryException;
    
}
