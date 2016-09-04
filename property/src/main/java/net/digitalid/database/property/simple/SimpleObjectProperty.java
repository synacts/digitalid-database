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
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeBuilder;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.dialect.ast.identifier.SQLBooleanAlias;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.ObjectProperty;
import net.digitalid.database.property.PropertyEntry;
import net.digitalid.database.property.PropertyEntryConverter;
import net.digitalid.database.testing.TestHost;

/**
 * A simple object property stores a simple value that is associated with an object in the database.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class SimpleObjectProperty<O, V> extends PersistentWritableSimpleProperty<V> implements ObjectProperty<O, V> {
    
    /* -------------------------------------------------- Loading -------------------------------------------------- */
    
    private boolean loaded = false;
    
    /**
     * Loads the time and value of this property from the database.
     */
    @Pure
    @NonCommitting
    protected void load() throws DatabaseException {
        try {
            PropertyEntryConverter<O, V, ?> converter = getTable().getConverter();
            final @Nullable PropertyEntry<O, V> entry = SQL.select(converter, SQLBooleanAlias.with("CLASSWITHSIMPLEPROPERTY_NAME.KEY = 123"), new TestHost() /* getObject().getSite() */);
            if (entry != null) {
                this.time = entry.getTime();
    //            this.value = entry.getValue(); TODO: Make sure to get a SimplePropertyEntry.
                this.loaded = true;
            }
        } catch (ExternalException ex) {
            throw new RuntimeException(ex);
        }
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
            final @Nonnull SimplePropertyEntrySubclass<O, V> entry = new SimplePropertyEntrySubclass<>(getObject(), newTime, newValue);
            try {
                // TODO: The old time and value might not be needed for this update.
                // TODO: getTable().replace(this, oldTime, newTime, oldValue, newValue);
                // TODO: Update instead of insert:
                SQL.insert(entry, (SimplePropertyEntryConverter<O, V>) getTable().getConverter(), new TestHost() /* getObject().getSite() */);
            } catch (ExternalException ex) {
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
