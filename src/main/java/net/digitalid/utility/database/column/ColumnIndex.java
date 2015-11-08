package net.digitalid.utility.database.column;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.database.converter.AbstractSQLConverter;

/**
 * This class represents the current column index in a database table.
 * This index is incremented by storing and retrieving {@link SQL} objects.
 * 
 * @see AbstractSQLConverter
 */
public final class ColumnIndex {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this index.
     */
    private int value;
    
    /**
     * Returns the value of this index.
     * 
     * @return the value of this index.
     */
    @Pure
    public int getValue() {
        return value;
    }
    
    /**
     * Increments the value of this index by one.
     */
    public void incrementValue() {
        value++;
    }
    
    /**
     * Returns the current value of this index and then increments it by one.
     * 
     * @return the current value of this index.
     */
    @Pure
    public int getAndIncrementValue() {
        return value++;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column index with the given value.
     * 
     * @param value the value of the new index.
     */
    private ColumnIndex(int value) {
        this.value = value;
    }
    
    /**
     * Creates a new column index with the given value.
     * 
     * @param value the value of the new index.
     * 
     * @return a new column index with the given value.
     */
    @Pure
    public static @Nonnull ColumnIndex get(int value) {
        return new ColumnIndex(value);
    }
    
    /**
     * Creates a new column index with the value zero.
     * 
     * @return a new column index with the value zero.
     */
    @Pure
    public static @Nonnull ColumnIndex get() {
        return new ColumnIndex(0);
    }
    
}
