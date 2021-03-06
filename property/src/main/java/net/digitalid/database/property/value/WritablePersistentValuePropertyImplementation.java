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

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.value.WritableValuePropertyImplementation;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeBuilder;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.lock.LockNotHeldByCurrentThread;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.WhereCondition;
import net.digitalid.database.conversion.WhereConditionBuilder;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.property.subject.Subject;
import net.digitalid.database.property.subject.SubjectUtility;

/**
 * This class implements the {@link WritablePersistentValueProperty}.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
public abstract class WritablePersistentValuePropertyImplementation<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Specifiable VALUE> extends WritableValuePropertyImplementation<VALUE, DatabaseException, RecoveryException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>> implements WritablePersistentValueProperty<SUBJECT, VALUE> {
    
    /* -------------------------------------------------- Validator -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Predicate<? super VALUE> getValueValidator() {
        return getTable().getValueValidator();
    }
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PersistentValuePropertyTable<UNIT, SUBJECT, VALUE, ?> getTable();
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    protected boolean loaded = false;
    
    /**
     * Loads the time and value of this property from the database.
     * 
     * @param locking whether this method acquires the non-reentrant lock.
     */
    @Pure
    @NonCommitting
    protected void load(final boolean locking) throws DatabaseException, RecoveryException {
        if (locking) { lock.lock(); }
        try {
            final @Nonnull Converter<SUBJECT, ?> subjectConverter = getTable().getParentModule().getSubjectTable();
            final @Nonnull WhereCondition<SUBJECT> whereCondition = WhereConditionBuilder.withConverter(subjectConverter).withObject(getSubject()).withPrefix(subjectConverter.getTypeName().toLowerCase()).build();
            final @Nullable PersistentValuePropertyEntry<SUBJECT, VALUE> entry = SQL.selectFirst(getTable(), getSubject().getUnit(), getSubject().getUnit(), whereCondition);
            if (entry != null) {
                this.time = entry.getTime();
                this.value = entry.getValue();
            } else {
                this.time = null;
                this.value = getTable().getDefaultValue();
            }
            this.loaded = true;
        } finally {
            if (locking) { lock.unlock(); }
        }
    }
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    protected @Nullable Time time;
    
    @Pure
    @Override
    @NonCommitting
    public @Nullable Time getTime() throws DatabaseException, RecoveryException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as both set(value) and reset() that call external code ensure that the time is loaded.
        return time;
    }
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    protected @Nullable @Valid VALUE value;
    
    @Pure
    @Override
    @NonCommitting
    public @Valid VALUE get() throws DatabaseException, RecoveryException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as both set(value) and reset() that call external code ensure that the value is loaded.
        return value;
    }
    
    @Impure
    @Override
    @Committing
    @LockNotHeldByCurrentThread
    public @Capturable @Valid VALUE set(@Captured @Valid VALUE newValue) throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            final @Valid VALUE oldValue = value;
            if (!Objects.equals(newValue, oldValue)) {
                final @Nonnull Time newTime = TimeBuilder.build();
                final @Nonnull PersistentValuePropertyEntry<SUBJECT, VALUE> entry = new PersistentValuePropertyEntrySubclass<>(getSubject(), newTime, newValue);
                SQL.insertOrReplace(getTable(), entry, getSubject().getUnit());
                this.time = newTime;
                this.value = newValue;
                Database.commit();
                notifyObservers(oldValue, newValue);
            } else { Database.commit(); }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }
    
    /* -------------------------------------------------- Combination -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    @LockNotHeldByCurrentThread
    public @Nonnull Pair<@Valid VALUE, @Nullable Time> getValueWithTimeOfLastModification() throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            return Pair.of(value, time);
        } finally {
            lock.unlock();
        }
    }
    
    /* -------------------------------------------------- Reset -------------------------------------------------- */
    
    @Impure
    @Override
    @NonCommitting
    @LockNotHeldByCurrentThread
    public void reset() throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (loaded) {
                if (observers.isEmpty()) {
                    this.loaded = false;
                } else {
                    final @Valid VALUE oldValue = value;
                    load(false);
                    final @Valid VALUE newValue = value;
                    if (!Objects.equals(newValue, oldValue)) {
                        notifyObservers(oldValue, newValue);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    @Override
    @CallSuper
    @PureWithSideEffects
    protected void initialize() {
        super.initialize();
        
        SubjectUtility.add(getSubject(), this);
        this.value = getTable().getDefaultValue();
    }
    
}
