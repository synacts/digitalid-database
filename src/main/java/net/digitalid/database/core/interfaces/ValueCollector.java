package net.digitalid.database.core.interfaces;

import java.io.InputStream;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedResourceClosingException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedValueStoringException;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.utility.collections.annotations.size.Size;
import net.digitalid.utility.collections.annotations.size.SizeAtMost;

/**
 * This interface allows to set the values of an SQL statement.
 * Advancing the parameter index is left to the implementation.
 */
public interface ValueCollector extends AutoCloseable {
    
    /* -------------------------------------------------- Setters -------------------------------------------------- */
    
    /**
     * Sets the next parameter to empty.
     */
    public void setEmpty() throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given boolean value.
     * 
     * @param value the boolean value which is to be set.
     */
    public void setBoolean(boolean value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given byte value.
     * 
     * @param value the byte value which is to be set.
     */
    public void setInteger08(byte value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given short value.
     * 
     * @param value the short value which is to be set.
     */
    public void setInteger16(short value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given int value.
     * 
     * @param value the int value which is to be set.
     */
    public void setInteger32(int value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given long value.
     * 
     * @param value the long value which is to be set.
     */
    public void setInteger64(long value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given integer value.
     * 
     * @param value the integer value which is to be set.
     */
    public void setInteger(@Nonnull BigInteger value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given float value.
     * 
     * @param value the float value which is to be set.
     */
    public void setDecimal32(float value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given double value.
     * 
     * @param value the double value which is to be set.
     */
    public void setDecimal64(double value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given char value.
     * 
     * @param value the char value which is to be set.
     */
    public void setString01(char value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given string value.
     * 
     * @param value the string value which is to be set.
     */
    public void setString64(@Nonnull @SizeAtMost(64) String value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given string value.
     * 
     * @param value the string value which is to be set.
     */
    public void setString(@Nonnull String value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given binary value.
     * 
     * @param value the binary value which is to be set.
     */
    public void setBinary128(@Nonnull @Size(16) byte[] value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given binary value.
     * 
     * @param value the binary value which is to be set.
     */
    public void setBinary256(@Nonnull @Size(32) byte[] value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given binary value.
     * 
     * @param value the binary value which is to be set.
     */
    public void setBinary(@Nonnull byte[] value) throws FailedValueStoringException;
    
    /**
     * Sets the next parameter to the given binary stream.
     * 
     * @param stream the binary stream which is to be set.
     * 
     * @require Database.getInstance().supportsBinaryStreams() : "The database supports binary streams.";
     */
    public void setBinaryStream(@Nonnull InputStream stream) throws FailedValueStoringException;
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
     * 
     * @param type the SQL type of the next parameter which is to be set to null.
     */
    public void setNull(@Nonnull SQLType type) throws FailedValueStoringException;
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    /**
     * Adds the current values to the batch of commands which are to be executed.
     */
    public void addBatch() throws FailedValueStoringException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Override
    public void close() throws FailedResourceClosingException;
    
}
