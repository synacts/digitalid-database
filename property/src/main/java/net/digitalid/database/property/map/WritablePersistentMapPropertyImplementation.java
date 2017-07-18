package net.digitalid.database.property.map;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.collections.map.FreezableMap;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.contracts.Validate;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.map.WritableMapPropertyImplementation;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.lock.LockNotHeldByCurrentThread;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;

/**
 * This class implements the {@link WritablePersistentMapProperty}.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
public abstract class WritablePersistentMapPropertyImplementation<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable KEY, @Unspecifiable VALUE, @Unspecifiable READONLY_MAP extends ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>, @Unspecifiable FREEZABLE_MAP extends FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> extends WritableMapPropertyImplementation<KEY, VALUE, READONLY_MAP, DatabaseException, RecoveryException, PersistentMapObserver<SUBJECT, KEY, VALUE, READONLY_MAP>, ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP>> implements WritablePersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP, FREEZABLE_MAP> {
    
    /* -------------------------------------------------- Validators -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Predicate<? super KEY> getKeyValidator() {
        return getTable().getKeyValidator();
    }
    
    @Pure
    @Override
    public @Nonnull Predicate<? super VALUE> getValueValidator() {
        return getTable().getValueValidator();
    }
    
    /* -------------------------------------------------- Map -------------------------------------------------- */
    
    @Pure
    protected abstract @Nonnull @NonFrozen FREEZABLE_MAP getMap();
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PersistentMapPropertyTable<UNIT, SUBJECT, KEY, VALUE, ?, ?> getTable();
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    protected boolean loaded = false;
    
    /**
     * Loads the key-value pairs of this property from the database.
     * 
     * @param locking whether this method acquires the non-reentrant lock.
     */
    @Pure
    @NonCommitting
    protected void load(final boolean locking) throws DatabaseException, RecoveryException {
        if (locking) { lock.lock(); }
        try {
            getMap().clear();
            final @Nonnull String prefix = getTable().getParentModule().getSubjectTable().getTypeName().toLowerCase();
            final @Nonnull @NonNullableElements FreezableList<PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>> entries = SQL.selectAll(getTable(), getSubject().getUnit(), getTable().getParentModule().getSubjectTable(), getSubject(), prefix, getSubject().getUnit());
            for (@Nonnull PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> entry : entries) {
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
    public @Nonnull @NonFrozen READONLY_MAP get() throws DatabaseException, RecoveryException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as add(key, value), remove(key, value) and reset() that call external code ensure that the map is loaded.
        return (READONLY_MAP) getMap();
    }
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Nullable @Valid VALUE get(@NonCaptured @Unmodified @Nonnull @Valid("key") KEY key) throws DatabaseException, RecoveryException {
        if (!loaded) { load(true); } // This should never trigger a reentrance exception as add(key, value), remove(key, value) and reset() that call external code ensure that the map is loaded.
        return getMap().get(key);
    }
    
    /* -------------------------------------------------- Operations -------------------------------------------------- */
    
    @Impure
    @Override
    @Committing
    @LockNotHeldByCurrentThread
    public boolean add(@Captured @Nonnull @Valid("key") KEY key, @Captured @Nonnull @Valid VALUE value) throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            if (getMap().containsKey(key)) {
                return false;
            } else {
                final @Nonnull PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> entry = new PersistentMapPropertyEntrySubclass<>(getSubject(), key, value);
                SQL.insertOrAbort(getTable(), entry, getSubject().getUnit());
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
    @LockNotHeldByCurrentThread
    public @Capturable @Nullable @Valid VALUE remove(@NonCaptured @Unmodified @Nonnull @Valid("key") KEY key) throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (!loaded) { load(false); }
            final @Nullable VALUE value = getMap().get(key);
            if (value != null) {
                final @Nonnull PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> entry = new PersistentMapPropertyEntrySubclass<>(getSubject(), key, value); // TODO: The value should actually not be necessary.
                SQL.delete(getTable(), getTable(), entry, getSubject().getUnit());
                getMap().remove(key);
                notifyObservers(key, value, false);
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
    @LockNotHeldByCurrentThread
    public void reset() throws DatabaseException, RecoveryException {
        lock.lock();
        try {
            if (loaded) {
                if (observers.isEmpty()) {
                    this.loaded = false;
                } else {
                    final @Nonnull FreezableMap<KEY, VALUE> oldMap = getMap().clone();
                    load(false);
                    final @Nonnull FreezableMap<KEY, VALUE> newMap = getMap();
                    for (Map.@Nonnull Entry<KEY, VALUE> entry : newMap.entrySet().exclude(oldMap.entrySet())) {
                        notifyObservers(entry.getKey(), entry.getValue(), true);
                    }
                    for (Map.@Nonnull Entry<KEY, VALUE> entry : oldMap.entrySet().exclude(newMap.entrySet())) {
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
