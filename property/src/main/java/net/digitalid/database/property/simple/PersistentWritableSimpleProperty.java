package net.digitalid.database.property.simple;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.simple.WritableSimpleProperty;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeBuilder;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;

/**
 * This writable property stores a simple value in the database.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentWritableSimpleProperty<O, V> extends WritableSimpleProperty<V, DatabaseException> implements PersistentProperty<O, V> {
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    private boolean loaded = false;
    
    /**
     * Loads the time and value of this property from the database.
     */
    @Pure
    @NonCommitting
    protected void load() throws DatabaseException {
        // TODO: Use the getTable() to load the time and value of this property from the database.
//        final @Nonnull @NonNullableElements ReadOnlyPair<Time, V> pair = propertySetup.getPropertyTable().load(this, propertySetup);
//        this.time = pair.getNonNullableElement0();
//        this.value = pair.getNonNullableElement1();
        this.loaded = true;
    }
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    private @Nullable Time time;
    
    @Pure
    @Override
    @NonCommitting
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
        Require.that(getTable().getValueValidator().evaluate(newValue)).orThrow("The new value has to be valid but was $.", newValue);
        
        final @Valid V oldValue = get();
        
        if (!Objects.equals(newValue, oldValue)) {
            final @Nullable Time oldTime = getTime();
            final @Nonnull Time newTime = TimeBuilder.build();
            // TODO: getTable().replace(this, oldTime, newTime, oldValue, newValue);
            // TODO: The old time and value might not be needed for this update.
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
