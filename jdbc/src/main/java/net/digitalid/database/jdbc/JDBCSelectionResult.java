package net.digitalid.database.jdbc;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.annotations.size.MaxSize;
import net.digitalid.utility.collections.annotations.size.Size;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.exceptions.state.value.CorruptParameterValueException;
import net.digitalid.database.core.interfaces.SelectionResult;

/**
 * This classes uses the JDBC result set to retrieve the values.
 */
public class JDBCSelectionResult implements SelectionResult {
    
    /* -------------------------------------------------- Result Set -------------------------------------------------- */
    
    /**
     * Stores the result set used to retrieve the values.
     */
    private final @Nonnull ResultSet resultSet;
    
    /**
     * Returns the result set used to retrieve the values.
     * 
     * @return the result set used to retrieve the values.
     */
    @Pure
    public @Nonnull ResultSet getResultSet() {
        return resultSet;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new selection result with the given result set.
     * 
     * @param resultSet the result set used to retrieve the values.
     */
    protected JDBCSelectionResult(@Nonnull ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    /**
     * Returns a new selection result with the given result set.
     * 
     * @param resultSet the result set used to retrieve the values.
     * 
     * @return a new selection result with the given result set.
     */
    @Pure
    public static @Nonnull JDBCSelectionResult get(@Nonnull ResultSet resultSet) {
        return new JDBCSelectionResult(resultSet);
    }
    
    /* -------------------------------------------------- Parameter Index -------------------------------------------------- */
    
    /**
     * Stores the index of the next column to get.
     */
    private int columnIndex = 1;
    
    /**
     * Returns the index of the next column to get.
     * 
     * @return the index of the next column to get.
     */
    @Pure
    public int getColumnIndex() {
        return columnIndex;
    }
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    @Override
    public boolean moveToNextRow() throws FailedValueRestoringException {
        try {
            columnIndex = 1;
            return resultSet.next();
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public void moveToFirstRow() throws FailedValueRestoringException, EntryNotFoundException {
        try {
            columnIndex = 1;
            if (!resultSet.first()) { throw EntryNotFoundException.get(); }
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    @Override
    public void getEmpty() throws FailedValueRestoringException {
        try {
            resultSet.getBoolean(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public boolean getBoolean() throws FailedValueRestoringException {
        try {
            return resultSet.getBoolean(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public byte getInteger08() throws FailedValueRestoringException {
        try {
            return resultSet.getByte(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public short getInteger16() throws FailedValueRestoringException {
        try {
            return resultSet.getShort(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public int getInteger32() throws FailedValueRestoringException {
        try {
            return resultSet.getInt(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public long getInteger64() throws FailedValueRestoringException {
        try {
            return resultSet.getLong(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable BigInteger getInteger() throws FailedValueRestoringException {
        try {
            final @Nullable byte [] bytes = resultSet.getBytes(columnIndex++);
            return bytes == null ? null : new BigInteger(bytes);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public float getDecimal32() throws FailedValueRestoringException {
        try {
            return resultSet.getFloat(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public double getDecimal64() throws FailedValueRestoringException {
        try {
            return resultSet.getDouble(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public char getString01() throws FailedValueRestoringException, CorruptNullValueException {
        try {
            final @Nullable String value = resultSet.getString(columnIndex++);
            if (value == null) { throw CorruptNullValueException.get(); }
            return value.charAt(0);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable @MaxSize(64) String getString64() throws FailedValueRestoringException, CorruptParameterValueException {
        try {
            final @Nullable String value = resultSet.getString(columnIndex++);
            if (value != null && value.length() > 64) { throw CorruptParameterValueException.get("string length", value.length()); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable String getString() throws FailedValueRestoringException {
        try {
            return resultSet.getString(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable @Size(16) byte[] getBinary128() throws FailedValueRestoringException, CorruptParameterValueException {
        try {
            final @Nullable byte[] value = resultSet.getBytes(columnIndex++);
            if (value != null && value.length != 16) { throw CorruptParameterValueException.get("binary length", value.length); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable @Size(32) byte[] getBinary256() throws FailedValueRestoringException, CorruptParameterValueException {
        try {
            final @Nullable byte[] value = resultSet.getBytes(columnIndex++);
            if (value != null && value.length != 32) { throw CorruptParameterValueException.get("binary length", value.length); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    @Override
    public @Nullable byte[] getBinary() throws FailedValueRestoringException {
        try {
            return resultSet.getBytes(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    @Override
    public boolean wasNull() throws FailedValueRestoringException {
        try {
            return resultSet.wasNull();
        } catch (@Nonnull SQLException exception) {
            throw FailedValueRestoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Override
    public void close() throws FailedResourceClosingException {
        try {
            resultSet.close();
        } catch (@Nonnull SQLException exception) {
            throw FailedResourceClosingException.get(exception);
        }
    }
    
}
