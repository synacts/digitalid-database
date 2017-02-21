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
import net.digitalid.database.subject.Subject;

/**
 * This read-only property stores a value in the persistent database.
 * 
 * @see WritablePersistentValueProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentValueProperty.class)
public interface ReadOnlyPersistentValueProperty<@Unspecifiable SUBJECT extends Subject<?>, @Specifiable VALUE> extends ReadOnlyValueProperty<VALUE, DatabaseException, RecoveryException, PersistentValueObserver<SUBJECT, VALUE>, ReadOnlyPersistentValueProperty<SUBJECT, VALUE>>, PersistentProperty<SUBJECT, PersistentValuePropertyEntry<SUBJECT, VALUE>, PersistentValueObserver<SUBJECT, VALUE>> {
    
    /* -------------------------------------------------- Getter -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Valid VALUE get() throws DatabaseException, RecoveryException;
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentValuePropertyTable<?, SUBJECT, VALUE, ?> getTable();
    
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
