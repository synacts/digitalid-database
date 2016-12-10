package net.digitalid.database.property.map;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;
import net.digitalid.utility.collections.map.FreezableMap;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.concurrency.exceptions.ReentranceException;
import net.digitalid.utility.contracts.Validate;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.map.WritableMapPropertyImplementation;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.dialect.ast.identifier.SQLBooleanAlias;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * This writable property stores a map of key-value pairs in the persistent database.
 * 
 * <em>Important:</em> Make sure that {@code F} is a sub-type of {@code R}!
 * Unfortunately, this cannot be enforced with the limited Java generics.
 * 
 * @invariant !get().keySet().containsNull() : "None of the keys may be null.";
 * @invariant !get().values().containsNull() : "None of the values may be null.";
 * @invariant get().keySet().matchAll(getKeyValidator()) : "Each key has to be valid.";
 * @invariant get().values().matchAll(getValueValidator()) : "Each value has to be valid.";
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
@Mutable(ReadOnlyPersistentMapProperty.class)
public abstract class WritablePersistentMapProperty<S extends Subject, K, V, R extends ReadOnlyMap<@Nonnull @Valid("key") K, @Nonnull @Valid V>, F extends FreezableMap<@Nonnull @Valid("key") K, @Nonnull @Valid V>> extends WritableMapPropertyImplementation<K, V, R, DatabaseException, ReadOnlyPersistentMapProperty.Observer<S, K, V, R>, ReadOnlyPersistentMapProperty<S, K, V, R>> implements ReadOnlyPersistentMapProperty<S, K, V, R> {
    
    /* -------------------------------------------------- Validators -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Predicate<? super K> getKeyValidator() {
        return getTable().getKeyValidator();
    }
    
    @Pure
    @Override
    public @Nonnull Predicate<? super V> getValueValidator() {
        return getTable().getValueValidator();
    }
    
    /* -------------------------------------------------- Map -------------------------------------------------- */
    
    @Pure
    protected abstract @Nonnull @NonFrozen F getMap();
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    protected boolean loaded = false;
    
    /**
     * Loads the key-value pairs of this property from the database.
     * 
     * @param locking whether this method acquires the non-reentrant lock.
     */
    @Pure
    @NonCommitting
    @TODO(task = "Instead of reading and adding a single entry, select and add all entries of the subject to the freezable map.", date = "2016-09-30", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.HIGH)
    protected void load(final boolean locking) throws DatabaseException, ReentranceException {
        if (locking) { lock.lock(); }
        try {
            getMap().clear();
            final @Nullable PersistentMapPropertyEntry<S, K, V> entry = SQL.select(getTable().getEntryConverter(), SQLBooleanAlias.with("key = 'TODO'"), getSubject().getSite(), getSubject().getSite());
            if (entry != null) {
                getMap().put(entry.getKey(), entry.getValue());
            }
            this.loaded = true;
        } finally {
            if (locking) { lock.unlock(); }
        }
    }
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    @SuppressWarnings("unchecked")
    public @Nonnull @NonFrozen R get() throws DatabaseException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as add(key, value), remove(key, value) and reset() that call external code ensure that the map is loaded.
        return (R) getMap();
    }
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Nullable @Valid V get(@NonCaptured @Unmodified @Nonnull @Valid("key") K key) throws DatabaseException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as add(key, value), remove(key, value) and reset() that call external code ensure that the map is loaded.
        return getMap().get(key);
    }
    
    /* -------------------------------------------------- Operations -------------------------------------------------- */
    
    @Impure
    @Override
    @Committing
    public boolean add(@Captured @Nonnull @Valid("key") K key, @Captured @Nonnull @Valid V value) throws DatabaseException, ReentranceException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            if (getMap().containsKey(key)) {
                return false;
            } else {
                final @Nonnull PersistentMapPropertyEntry<S, K, V> entry = new PersistentMapPropertyEntrySubclass<>(getSubject(), key, value);
                SQL.insert(entry, getTable().getEntryConverter(), getSubject().getSite());
                getMap().put(key, value);
                notifyObservers(key, value, true);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Impure
    @Override
    @Committing
    @TODO(task = "Implement and use SQL.delete().", date = "2016-09-30", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.HIGH)
    public @Capturable @Nullable @Valid V remove(@NonCaptured @Unmodified @Nonnull @Valid("key") K key) throws DatabaseException, ReentranceException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            if (getMap().containsKey(key)) {
                // TODO (of course without SQL injection!): SQL.delete(getTable().getEntryConverter(), SQLBooleanAlias.with("key = " + key), getSubject().getSite());
                final @Nullable V value = getMap().remove(key);
                if (value != null) { notifyObservers(key, value, false); }
                else { throw UnexpectedValueException.with("value", value); }
                return value;
            } else {
                return null;
            }
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
                    final @Nonnull FreezableMap<K, V> oldMap = getMap().clone();
                    load(false);
                    final @Nonnull FreezableMap<K, V> newMap = getMap();
                    for (Map.@Nonnull Entry<K, V> entry : newMap.entrySet().exclude(oldMap.entrySet())) {
                        notifyObservers(entry.getKey(), entry.getValue(), true);
                    }
                    for (Map.@Nonnull Entry<K, V> entry : oldMap.entrySet().exclude(newMap.entrySet())) {
                        notifyObservers(entry.getKey(), entry.getValue(), false);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /* -------------------------------------------------- Validate -------------------------------------------------- */
    
    @Pure
    @Override
    @CallSuper
    public void validate() {
        super.validate();
        Validate.that(!getMap().keySet().containsNull()).orThrow("None of the keys may be null.");
        Validate.that(!getMap().values().containsNull()).orThrow("None of the values may be null.");
        Validate.that(getMap().keySet().matchAll(getKeyValidator())).orThrow("Each key has to be valid.");
        Validate.that(getMap().values().matchAll(getValueValidator())).orThrow("Each value has to be valid.");
    }
    
}
