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
package net.digitalid.database.property.subject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collections.collection.ReadOnlyCollection;
import net.digitalid.utility.collections.map.FreezableLinkedHashMap;
import net.digitalid.utility.collections.map.FreezableLinkedHashMapBuilder;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.threading.annotations.MainThread;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Provided;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.property.annotations.GenerateSubjectModule;

/**
 * A subject belongs to a {@link Unit database unit} and can have persistent properties.
 */
@Immutable
@TODO(task = "Introduce a superinterface ReferenceableObject without the methods of this interface?", date = "2017-04-15", author = Author.KASPAR_ETTER)
public abstract class Subject<@Unspecifiable UNIT extends Unit> extends RootClass {
    
    /* -------------------------------------------------- Unit -------------------------------------------------- */
    
    /**
     * Returns the unit to which this subject belongs.
     */
    @Pure
    @Provided
    @Default("net.digitalid.utility.storage.interfaces.Unit.DEFAULT")
    public abstract @Nonnull UNIT getUnit();
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    /**
     * Generates and returns the {@link SubjectModule} required to store persistent properties.
     */
    @Pure
    @GenerateSubjectModule
    @TODO(task = "Make it possible that this method can be called getModule() without generating a corresponding field.", date = "2016-12-11", author = Author.KASPAR_ETTER)
    public abstract @Nullable SubjectModule<UNIT, ?> module();
    
    /* -------------------------------------------------- Properties -------------------------------------------------- */
    
    /**
     * Stores the persistent properties of this subject.
     */
    private final @Nonnull @NonFrozen FreezableLinkedHashMap<@Nonnull PersistentPropertyTable<?, ?, ?>, @Nonnull PersistentProperty<?, ?>> properties = FreezableLinkedHashMapBuilder.build();
    
    /**
     * Adds the given persistent property to this subject.
     * 
     * @require property.getSubject() == this : "The given persistent property belongs to this subject.";
     */
    @MainThread
    @PureWithSideEffects // This class is immutable after initialization.
    void add(@Nonnull PersistentProperty<?, ?> property) {
        Require.that(property.getSubject() == this).orThrow("The given persistent property has to belong to this subject.");
        
        properties.put(property.getTable(), property);
    }
    
    /**
     * Returns the persistent properties of this subject.
     */
    @Pure
    public @Nonnull @NonFrozen @NonNullableElements ReadOnlyCollection<PersistentProperty<?, ?>> getProperties() {
        return properties.values();
    }
    
    /**
     * Returns whether this subject has a persistent property with the given table.
     */
    @Pure
    public boolean hasProperty(@Nonnull PersistentPropertyTable<?, ?, ?> table) {
        return properties.containsKey(table);
    }
    
    /**
     * Returns the persistent property of this subject with the given table.
     * 
     * @require hasProperty(table) : "This subject has a persistent property with the given table.";
     */
    @Pure
    public @Nonnull PersistentProperty<?, ?> getProperty(@Nonnull PersistentPropertyTable<?, ?, ?> table) {
        Require.that(hasProperty(table)).orThrow("This subject has to have a persistent property with the given table.");
        
        return properties.get(table);
    }
    
    /**
     * Resets all persistent properties of this subject.
     */
    @Pure
    @NonCommitting
    public void resetAllProperties() throws DatabaseException, RecoveryException {
        for (final @Nonnull PersistentProperty<?, ?> property : getProperties()) { property.reset(); }
    }
    
}
