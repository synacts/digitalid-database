package net.digitalid.database.core.interfaces;

import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.conversion.converter.ValueCollector;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This interface allows to set the values of an SQL statement.
 * Advancing the parameter index is left to the implementation.
 */
public interface SQLValueCollector extends AutoCloseable, ValueCollector {
    
    /* -------------------------------------------------- Setters -------------------------------------------------- */
    
    /**
     * Sets the next parameter to empty.
     */
    @Impure
    public void setEmpty() throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given boolean value.
     */
    @Impure
    public void setBoolean(boolean value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given byte value.
     */
    @Impure
    public void setInteger08(byte value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given short value.
     */
    @Impure
    public void setInteger16(short value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given int value.
     */
    @Impure
    public void setInteger32(int value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given long value.
     */
    @Impure
    public void setInteger64(long value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given integer value.
     */
    @Impure
    public void setInteger(@Nonnull BigInteger value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given float value.
     */
    @Impure
    public void setDecimal32(float value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given double value.
     */
    @Impure
    public void setDecimal64(double value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given char value.
     */
    @Impure
    public void setString01(char value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given string value.
     */
    @Impure
    public void setString64(@Nonnull @MaxSize(64) String value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given string value.
     */
    @Impure
    public void setString(@Nonnull String value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given binary value.
     */
    @Impure
    public void setBinary128(@Nonnull @Size(16) byte[] value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given binary value.
     */
    @Impure
    public void setBinary256(@Nonnull @Size(32) byte[] value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given binary value.
     */
    @Impure
    public void setBinary(@Nonnull byte[] value) throws FailedSQLValueConversionException;
    
    /**
     * Sets the next parameter to the given input stream.
     * 
     * @param stream the input stream which is to be set.
     * @param length the number of bytes in the input stream.
     * 
     * @require Database.getInstance().supportsBinaryStreams() : "The database supports binary streams.";
     */
    @Impure
    public void setBinaryStream(@Nonnull InputStream stream, int length) throws FailedSQLValueConversionException;
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
     * 
     * @param typeCode the SQL type of the next parameter which is to be set to null.
     */
    @Impure
    public void setNull(int typeCode) throws FailedSQLValueConversionException;
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    /**
     * Adds the current values to the batch of commands which are to be executed.
     */
    @Impure
    public void addBatch() throws FailedSQLValueConversionException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws FailedResourceClosingException;
    
}
