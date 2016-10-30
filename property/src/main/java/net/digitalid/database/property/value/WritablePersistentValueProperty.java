package net.digitalid.database.property.value;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;
import net.digitalid.utility.concurrency.exceptions.ReentranceException;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.value.WritableValuePropertyImplementation;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.auxiliary.Time;
import net.digitalid.database.auxiliary.TimeBuilder;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.dialect.ast.identifier.SQLBooleanAlias;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.Subject;

/**
 * This writable property stores a value in the persistent database.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
@Mutable(ReadOnlyPersistentValueProperty.class)
public abstract class WritablePersistentValueProperty<S extends Subject, V> extends WritableValuePropertyImplementation<V, DatabaseException, ReadOnlyPersistentValueProperty.Observer<S, V>, ReadOnlyPersistentValueProperty<S, V>> implements ReadOnlyPersistentValueProperty<S, V> {
    
    /* -------------------------------------------------- Validator -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Predicate<? super V> getValueValidator() {
        return getTable().getValueValidator();
    }
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    private boolean loaded = false;
    
    /**
     * Loads the time and value of this property from the database.
     * 
     * @param locking whether this method acquires the non-reentrant lock.
     */
    @Pure
    @NonCommitting
    protected void load(final boolean locking) throws DatabaseException, ReentranceException {
        if (locking) { lock.lock(); }
        try {
            final @Nullable ValuePropertyEntry<S, V> entry = SQL.select(getTable().getEntryConverter(), SQLBooleanAlias.with("key = 123"), getSubject().getSite(), getSubject().getSite());
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
    
    private @Nullable Time time;
    
    @Pure
    @Override
    @NonCommitting
    public @Nullable Time getTime() throws DatabaseException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as both set(value) and reset() that call external code ensure that the time is loaded.
        return time;
    }
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    private @Nullable @Valid V value;
    
    @Pure
    @Override
    @NonCommitting
    public @Valid V get() throws DatabaseException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as both set(value) and reset() that call external code ensure that the value is loaded.
        return value;
    }
    
    @Impure
    @Override
    @Committing
    @TODO(task = "Implement and use SQL.update() instead of using SQL.insert().", date = "2016-09-27", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.HIGH)
    public @Capturable @Valid V set(@Captured @Valid V newValue) throws DatabaseException, ReentranceException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            final @Valid V oldValue = value;
            if (!Objects.equals(newValue, oldValue)) {
                final @Nonnull Time newTime = TimeBuilder.build();
                final @Nonnull ValuePropertyEntry<S, V> entry = new ValuePropertyEntrySubclass<>(getSubject(), newTime, newValue);
                SQL.insert(entry, getTable().getEntryConverter(), getSubject().getSite());
                this.time = newTime;
                this.value = newValue;
                notifyObservers(oldValue, newValue);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }
    
    /* -------------------------------------------------- Combination -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @Nonnull Pair<@Valid V, @Nullable Time> getValueWithTimeOfLastModification() throws DatabaseException, ReentranceException {
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
    public void reset() throws DatabaseException, ReentranceException {
        lock.lock();
        try {
            if (loaded) {
                if (observers.isEmpty()) {
                    this.loaded = false;
                } else {
                    final @Valid V oldValue = value;
                    load(false);
                    final @Valid V newValue = value;
                    if (!Objects.equals(newValue, oldValue)) {
                        notifyObservers(oldValue, newValue);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
}
