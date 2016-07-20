package net.digitalid.database.core.interfaces;

import java.math.BigInteger;

import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.SelectionResult;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;

/**
 * This interface allows to get the values of an SQL result.
 * Advancing the column index is left to the implementation.
 */
public interface SQLSelectionResult extends AutoCloseable, SelectionResult {
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    /**
     * Moves the cursor to the next row of the selection result.
     * 
     * @return whether there was another row to which the cursor could be moved.
     */
    @Impure
    public boolean moveToNextRow();
    
    /**
     * Moves the cursor to the first row of the selection result.
     * 
     * @throws EntryNotFoundException if the selection results contains no rows.
     */
    @Impure
    public void moveToFirstRow() throws EntryNotFoundException;
    
    /**
     * Returns the current position of the cursor in the columns.
     */
    @Pure
    public int getColumnIndex();
    
    /**
     * Moves the cursor to the column specified in the parameter.
     * 
     * @param columnIndex the column to which the cursor should be moved.
     */
    @Impure
    public void moveToColumn(int columnIndex);
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    /**
     * Returns nothing from the next column.
     */
    @Impure
    public void getEmpty();
    
    /**
     * Returns the boolean value of the next column.
     */
    @Impure
    public boolean getBoolean();
    
    /**
     * Returns the byte value of the next column.
     */
    @Impure
    public byte getInteger08();
    
    /**
     * Returns the short value of the next column.
     */
    @Impure
    public short getInteger16();
    
    /**
     * Returns the int value of the next column.
     */
    @Impure
    public int getInteger32();
    
    /**
     * Returns the long value of the next column.
     */
    @Impure
    public long getInteger64();
    
    /**
     * Returns the integer value of the next column.
     */
    @Impure
    public @Nullable BigInteger getInteger();
    
    /**
     * Returns the float value of the next column.
     */
    @Impure
    public float getDecimal32();
    
    /**
     * Returns the double value of the next column.
     */
    @Impure
    public double getDecimal64();
    
    /**
     * Returns the char value of the next column.
     */
    @Impure
    public char getString01();
    
    /**
     * Returns the string value of the next column.
     */
    @Impure
    public @Nullable @MaxSize(64) String getString64();
    
    /**
     * Returns the string value of the next column.
     */
    @Impure
    public @Nullable String getString();
    
    /**
     * Returns the binary value of the next column.
     */
    @Impure
    public @Nullable @Size(16) byte[] getBinary128();
    
    /**
     * Returns the binary value of the next column.
     */
    @Impure
    public @Nullable @Size(32) byte[] getBinary256();
    
    /**
     * Returns the binary value of the next column.
     */
    @Impure
    public @Nullable byte[] getBinary();
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Returns whether the last returned column was null.
     */
    @Pure
    public boolean wasNull();
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws FailedResourceClosingException;
    
}
