package net.digitalid.database.core.interfaces;

import java.math.BigInteger;
import javax.annotation.Nullable;
import net.digitalid.database.core.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.exceptions.state.value.CorruptParameterValueException;
import net.digitalid.utility.collections.annotations.size.Size;
import net.digitalid.utility.collections.annotations.size.SizeAtMost;

/**
 * This interface allows to get the values of an SQL result set.
 * Advancing the column index is left to the implementation.
 */
public interface SelectionResult extends AutoCloseable {
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    /**
     * Moves the cursor to the next row of the selection result.
     * 
     * @return whether there was another row to which the cursor could be moved.
     */
    public boolean moveToNextRow() throws FailedValueRestoringException;
    
    /**
     * Moves the cursor to the first row of the selection result.
     * 
     * @throws EntryNotFoundException if the selection results contains no rows.
     */
    public void moveToFirstRow() throws FailedValueRestoringException, EntryNotFoundException;
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    /**
     * Returns nothing from the next column.
     */
    public void getEmpty() throws FailedValueRestoringException;
    
    /**
     * Returns the boolean value of the next column.
     * 
     * @return the boolean value of the next column.
     */
    public boolean getBoolean() throws FailedValueRestoringException;
    
    /**
     * Returns the byte value of the next column.
     * 
     * @return the byte value of the next column.
     */
    public byte getInteger08() throws FailedValueRestoringException;
    
    /**
     * Returns the short value of the next column.
     * 
     * @return the short value of the next column.
     */
    public short getInteger16() throws FailedValueRestoringException;
    
    /**
     * Returns the int value of the next column.
     * 
     * @return the int value of the next column.
     */
    public int getInteger32() throws FailedValueRestoringException;
    
    /**
     * Returns the long value of the next column.
     * 
     * @return the long value of the next column.
     */
    public long getInteger64() throws FailedValueRestoringException;
    
    /**
     * Returns the integer value of the next column.
     * 
     * @return the integer value of the next column.
     */
    public @Nullable BigInteger getInteger() throws FailedValueRestoringException;
    
    /**
     * Returns the float value of the next column.
     * 
     * @return the float value of the next column.
     */
    public float getDecimal32() throws FailedValueRestoringException;
    
    /**
     * Returns the double value of the next column.
     * 
     * @return the double value of the next column.
     */
    public double getDecimal64() throws FailedValueRestoringException;
    
    /**
     * Returns the char value of the next column.
     * 
     * @return the char value of the next column.
     */
    public char getString01() throws FailedValueRestoringException, CorruptNullValueException;
    
    /**
     * Returns the string value of the next column.
     * 
     * @return the string value of the next column.
     */
    public @Nullable @SizeAtMost(64) String getString64() throws FailedValueRestoringException, CorruptParameterValueException;
    
    /**
     * Returns the string value of the next column.
     * 
     * @return the string value of the next column.
     */
    public @Nullable String getString() throws FailedValueRestoringException;
    
    /**
     * Returns the binary value of the next column.
     * 
     * @return the binary value of the next column.
     */
    public @Nullable @Size(16) byte[] getBinary128() throws FailedValueRestoringException, CorruptParameterValueException;
    
    /**
     * Returns the binary value of the next column.
     * 
     * @return the binary value of the next column.
     */
    public @Nullable @Size(32) byte[] getBinary256() throws FailedValueRestoringException, CorruptParameterValueException;
    
    /**
     * Returns the binary value of the next column.
     * 
     * @return the binary value of the next column.
     */
    public @Nullable byte[] getBinary() throws FailedValueRestoringException;
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Returns whether the last returned column was null.
     * 
     * @return whether the last returned column was null.
     */
    public boolean wasNull() throws FailedValueRestoringException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Override
    public void close() throws FailedResourceClosingException;
    
}
