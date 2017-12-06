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
package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.property.Observer;
import net.digitalid.utility.property.Property;
import net.digitalid.utility.validation.annotations.lock.LockNotHeldByCurrentThread;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.map.ReadOnlyPersistentMapProperty;
import net.digitalid.database.property.set.ReadOnlyPersistentSetProperty;
import net.digitalid.database.property.subject.Subject;
import net.digitalid.database.property.value.ReadOnlyPersistentValueProperty;

/**
 * A persistent property belongs to a {@link Subject subject} and stores its values in the database with the subject used as the key.
 * 
 * @see ReadOnlyPersistentMapProperty
 * @see ReadOnlyPersistentSetProperty
 * @see ReadOnlyPersistentValueProperty
 */
@Mutable
@ThreadSafe
public interface PersistentProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable OBSERVER extends Observer> extends Property<OBSERVER> {
    
    /* -------------------------------------------------- Subject -------------------------------------------------- */
    
    /**
     * Returns the subject to which this property belongs.
     */
    @Pure
    public @Nonnull SUBJECT getSubject();
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the property table that contains the property name, subject module and entry converter.
     */
    @Pure
    public @Nonnull PersistentPropertyTable<?, SUBJECT, ?> getTable();
    
    /* -------------------------------------------------- Reset -------------------------------------------------- */
    
    /**
     * Resets the values of this property so that they have to be reloaded from the database on the next retrieval.
     * If the state of the database changed in the meantime, then this method is impure.
     * However, read-only properties must be able to expose this method as well.
     */
    @Pure
    @NonCommitting
    @LockNotHeldByCurrentThread
    public void reset() throws DatabaseException, RecoveryException;
    
}
