package net.digitalid.database.jdbc;

import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.functional.failable.FailableProducer;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.core.interfaces.SQLSelectionResult;
import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedSQLValueRecoveryException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.exceptions.state.value.CorruptParameterValueException;

/**
 * This classes uses the JDBC result set to retrieve the values.
 */
public class JDBCSelectionResult implements SQLSelectionResult {
    
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
    @Override
    public int getColumnIndex() {
        return columnIndex;
    }
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    @Impure
    @Override
    public void moveToFirstColumn() {
        columnIndex = 1;
    }
    
    @Impure
    @Override
    public boolean moveToNextRow() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.next();
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void moveToFirstRow() throws FailedSQLValueRecoveryException {
        try {
            columnIndex = 1;
            if (!resultSet.first()) { throw EntryNotFoundException.get(); }
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    public void moveToColumn(int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    @Impure
    @Override
    public void getEmpty() throws FailedSQLValueRecoveryException {
        try {
            resultSet.getBoolean(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public boolean getBoolean() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getBoolean(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public byte getInteger08() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getByte(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public short getInteger16() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getShort(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public int getInteger32() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getInt(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public long getInteger64() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getLong(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable BigInteger getInteger() throws FailedSQLValueRecoveryException {
        try {
            final @Nullable byte [] bytes = resultSet.getBytes(columnIndex++);
            return bytes == null ? null : new BigInteger(bytes);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public float getDecimal32() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getFloat(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public double getDecimal64() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getDouble(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public char getString01() throws FailedSQLValueRecoveryException {
        try {
            final @Nullable String value = resultSet.getString(columnIndex++);
            if (value == null) { throw CorruptNullValueException.get(); }
            return value.charAt(0);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable @MaxSize(64) String getString64() throws FailedSQLValueRecoveryException {
        try {
            final @Nullable String value = resultSet.getString(columnIndex++);
            if (value != null && value.length() > 64) { throw CorruptParameterValueException.get("string length", value.length()); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable String getString() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getString(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable @Size(16) byte[] getBinary128() throws FailedSQLValueRecoveryException {
        try {
            final @Nullable byte[] value = resultSet.getBytes(columnIndex++);
            if (value != null && value.length != 16) { throw CorruptParameterValueException.get("binary length", value.length); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable @Size(32) byte[] getBinary256() throws FailedSQLValueRecoveryException {
        try {
            final @Nullable byte[] value = resultSet.getBytes(columnIndex++);
            if (value != null && value.length != 32) { throw CorruptParameterValueException.get("binary length", value.length); }
            return value;
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public @Nullable byte[] getBinary() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.getBytes(columnIndex++);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public <T> @Nullable List<T> getList(@Nonnull FailableProducer<@Nullable T, FailedSQLValueRecoveryException> function) throws FailedSQLValueRecoveryException {
        try {
            final @Nonnull ArrayList<@Nullable T> result = FreezableArrayList.withNoElements();
            final int beginsAtColumn = columnIndex;
            int listIndex = resultSet.getInt(columnIndex++);
            int last = listIndex;
            boolean hasAnotherRow = true;
            while (hasAnotherRow && !resultSet.wasNull()) {
                while (last < listIndex) {
                    result.add(null);
                    last++;
                }
                final @Nullable T object = function.produce();
                result.add(object);
                last++;
                hasAnotherRow = moveToNextRow();
                if (hasAnotherRow) {
                    moveToColumn(beginsAtColumn);
                    listIndex = resultSet.getInt(columnIndex++);
                    if (listIndex < last) {
                        hasAnotherRow = false;
                    }
                } else {
                    // move one row back
                    resultSet.relative(-1);
                }
            }
            return result;
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    @Impure
    @Override
    public <T> T[] getArray(@Nonnull FailableProducer<T, FailedSQLValueRecoveryException> function) throws FailedSQLValueRecoveryException {
        return (T[]) getList(function).toArray();
    }
    
    @Impure
    // TODO: implement
    @Override
    public <T> Set<T> getSet(@Nonnull FailableProducer<T, FailedSQLValueRecoveryException> function) {
        return null;
    }
    
    @Impure
    // TODO: implement
    @Override
    public <K, V> Map<K, V> getMap(@Nonnull FailableProducer<K, FailedSQLValueRecoveryException> keyFunction, @Nonnull FailableProducer<V, FailedSQLValueRecoveryException> valueFunction) {
        return null;
    }
    
    /* -------------------------------------------------- Transformational Types -------------------------------------------------- */
    
    @Impure
    @Override
    public void setDecryptionCipher(@Nonnull Cipher cipher) {
        // TODO: implement this if we allow encryption in the database.
    }
    
    @Impure
    @Override
    public void popDecryptionCipher() {
        // TODO: implement this if we allow encryption in the database.
    }
    
    @Impure
    @Override
    public void setDecompression(@Nonnull Inflater inflater) {
        // TODO: implement this if we allow compression in the database.
    }
    
    @Impure
    @Override
    public void popDecompression() {
        // TODO: implement this if we allow compression in the database.
    }
    
    @Impure
    @Override
    public void setSignatureDigest(@Nonnull MessageDigest digest) {
        // TODO: implement this if we allow signatures in the database.
    }
    
    @Impure
    @Override
    public @Nonnull DigestInputStream popSignatureDigest() {
        // TODO: implement this if we allow signatures in the database.
        return null;
    }
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean wasNull() throws FailedSQLValueRecoveryException {
        try {
            return resultSet.wasNull();
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueRecoveryException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws FailedResourceClosingException {
        try {
            resultSet.close();
        } catch (@Nonnull SQLException exception) {
            throw FailedResourceClosingException.get(exception);
        }
    }
    
}
