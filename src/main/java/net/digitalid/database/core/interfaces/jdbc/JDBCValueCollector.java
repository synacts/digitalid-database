package net.digitalid.database.core.interfaces.jdbc;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.size.Size;
import net.digitalid.utility.collections.annotations.size.SizeAtMost;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
public class JDBCValueCollector implements ValueCollector {
    
    /* -------------------------------------------------- Prepared Statement -------------------------------------------------- */
    
    /**
     * Stores the prepared statement used to collect the values.
     */
    private final @Nonnull PreparedStatement preparedStatement;
    
    /**
     * Returns the prepared statement used to collect the values.
     * 
     * @return the prepared statement used to collect the values.
     */
    @Pure
    public @Nonnull PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new value collector with the given prepared statement.
     * 
     * @param preparedStatement the prepared statement used to collect the values.
     */
    protected JDBCValueCollector(@Nonnull PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
    
    /**
     * Returns a new value collector with the given prepared statement.
     * 
     * @param preparedStatement the prepared statement used to collect the values.
     * 
     * @return a new value collector with the given prepared statement.
     */
    @Pure
    public static @Nonnull JDBCValueCollector get(@Nonnull PreparedStatement preparedStatement) {
        return new JDBCValueCollector(preparedStatement);
    }
    
    /* -------------------------------------------------- Parameter Index -------------------------------------------------- */
    
    /**
     * Stores the index of the next parameter to set.
     */
    private int parameterIndex = 1;
    
    /**
     * Returns the index of the next parameter to set.
     * 
     * @return the index of the next parameter to set.
     */
    @Pure
    public int getParameterIndex() {
        return parameterIndex;
    }
    
    /* -------------------------------------------------- Setters -------------------------------------------------- */
    
    @Override
    public void setEmpty() throws FailedValueStoringException {
        try {
            preparedStatement.setBoolean(parameterIndex++, true);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setBoolean(boolean value) throws FailedValueStoringException {
        try {
            preparedStatement.setBoolean(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setInteger08(byte value) throws FailedValueStoringException {
        try {
            preparedStatement.setByte(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setInteger16(short value) throws FailedValueStoringException {
        try {
            preparedStatement.setShort(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setInteger32(int value) throws FailedValueStoringException {
        try {
            preparedStatement.setInt(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setInteger64(long value) throws FailedValueStoringException {
        try {
            preparedStatement.setLong(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setInteger(@Nonnull BigInteger value) throws FailedValueStoringException {
        try {
            preparedStatement.setBytes(parameterIndex++, value.toByteArray());
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setDecimal32(float value) throws FailedValueStoringException {
        try {
            preparedStatement.setFloat(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setDecimal64(double value) throws FailedValueStoringException {
        try {
            preparedStatement.setDouble(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setString01(char value) throws FailedValueStoringException {
        try {
            preparedStatement.setString(parameterIndex++, String.valueOf(value));
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setString64(@Nonnull @SizeAtMost(64) String value) throws FailedValueStoringException {
        assert value.length() <= 64 : "The length of the string is at most 64.";
        
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setString(@Nonnull String value) throws FailedValueStoringException {
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setBinary128(@Nonnull @Size(16) byte[] value) throws FailedValueStoringException {
        assert value.length == 16 : "The length of the byte array is 16.";
        
        try {
            preparedStatement.setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setBinary256(@Nonnull @Size(32) byte[] value) throws FailedValueStoringException {
        assert value.length == 32 : "The length of the byte array is 32.";
        
        try {
            preparedStatement.setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setBinary(@Nonnull byte[] value) throws FailedValueStoringException {
        try {
            preparedStatement.setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    @Override
    public void setBinaryStream(@Nonnull InputStream stream, int length) throws FailedValueStoringException {
        assert Database.getInstance().supportsBinaryStreams() : "The database supports binary streams.";
        
        try {
            preparedStatement.setBinaryStream(parameterIndex++, stream, length);
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    @Override
    public void setNull(@Nonnull SQLType type) throws FailedValueStoringException {
        try {
            preparedStatement.setNull(parameterIndex++, type.getCode());
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    @Override
    public void addBatch() throws FailedValueStoringException {
        try {
            preparedStatement.addBatch();
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Override
    public void close() throws FailedResourceClosingException {
        try {
            preparedStatement.close();
        } catch (@Nonnull SQLException exception) {
            throw FailedResourceClosingException.get(exception);
        }
    }
    
}
