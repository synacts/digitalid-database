package net.digitalid.database.property.value;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.property.value.WritableValuePropertyImplementation;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeBuilder;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.dialect.ast.identifier.SQLBooleanAlias;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.Subject;

/**
 * This writable property stores a value in the persistent database.
 */
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
     */
    @Pure
    @NonCommitting
    protected void load() throws DatabaseException {
        try {
            final @Nonnull ValuePropertyEntryConverter<S, V> converter = getTable().getEntryConverter();
            final @Nullable ValuePropertyEntry<S, V> entry = SQL.select(converter, SQLBooleanAlias.with("key = 123"), getSubject().getSite());
            if (entry != null) {
                this.time = entry.getTime();
                this.value = entry.getValue();
                this.loaded = true;
            } // TODO: Set loaded in any case and use some default value instead?
        } catch (ExternalException ex) {
            // TODO: Make sure that SQL.select throws a DatabaseException instead of an ExternalException.
            throw new RuntimeException(ex);
        }
    }
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    private @Nullable Time time;
    
    @Pure
    @Override
    @NonCommitting
    @TODO(task = "Determine whether to return null or the earliest time if no value was set and make sure that the read-only version has the same contract.", date = "2016-09-24", author = Author.KASPAR_ETTER)
    public @Nullable Time getTime() throws DatabaseException {
        if (!loaded) { load(); }
        return time;
    }
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    private @Valid V value;
    
    @Pure
    @Override
    @NonCommitting
    public @Valid V get() throws DatabaseException {
        if (!loaded) { load(); }
        return value;
    }
    
    @Impure
    @Override
    @Committing
    public @Capturable @Valid V set(@Captured @Valid V newValue) throws DatabaseException {
        final @Valid V oldValue = get();
        
        if (!Objects.equals(newValue, oldValue)) {
            final @Nullable Time oldTime = getTime();
            final @Nonnull Time newTime = TimeBuilder.build();
            final @Nonnull ValuePropertyEntry<S, V> entry = new ValuePropertyEntrySubclass<>(getSubject(), newTime, newValue);
            try {
                // TODO: The old time and value might not be needed for this update.
                // TODO: getTable().replace(this, oldTime, newTime, oldValue, newValue);
                // TODO: Update instead of insert:
                SQL.insert(entry, getTable().getEntryConverter(), getSubject().getSite());
            } catch (ExternalException ex) {
                // TODO: SQL.insert throws the wrong exception as well.
                throw new RuntimeException(ex);
            }
            this.time = newTime;
            this.value = newValue;
            notifyObservers(oldValue, newValue);
        }
        
        return oldValue;
    }
    
    /* -------------------------------------------------- Reset -------------------------------------------------- */
    
    @Impure
    @Override
    @NonCommitting
    public void reset() throws DatabaseException {
        if (hasObservers() && loaded) {
            final @Valid V oldValue = get();
            this.loaded = false;
            final @Valid V newValue = get();
            if (!Objects.equals(newValue, oldValue)) {
                notifyObservers(oldValue, newValue);
            }
        } else {
            this.loaded = false;
        }
    }
    
}
