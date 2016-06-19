package net.digitalid.database.jdbc;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.functional.interfaces.Consumer;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
public class JDBCValueCollector implements SQLValueCollector {
    
    /* -------------------------------------------------- Prepared Statements -------------------------------------------------- */
    
    private final @Nonnull FiniteIterable<PreparedStatement> preparedStatements;
    
    @Pure
    public @Nonnull FiniteIterable<PreparedStatement> getPreparedStatements() {
        return preparedStatements;
    }
    
    /* -------------------------------------------------- Order of Execution -------------------------------------------------- */
    
    private final @Nonnull Queue<Integer> orderOfExecution;
    
    /**
     * Creates a new value collector with the given prepared statement.
     * 
     * @param preparedStatements the prepared statements used to collect the values.
     */
    protected JDBCValueCollector(@Nonnull FiniteIterable<PreparedStatement> preparedStatements, @Nonnull Queue<Integer> orderOfExecution) {
        this.preparedStatements = preparedStatements;
        this.orderOfExecution = orderOfExecution;
    }
    
    /**
     * Returns a new value collector with the given prepared statement.
     * 
     * @param preparedStatements the prepared statement used to collect the values.
     * 
     * @return a new value collector with the given prepared statement.
     */
    @Pure
    public static @Nonnull JDBCValueCollector get(@Nonnull FiniteIterable<PreparedStatement> preparedStatements, @Nonnull Queue<Integer> orderOfExecution) {
        return new JDBCValueCollector(preparedStatements, orderOfExecution);
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
    
    @Impure
    @Override
    public void setEmpty() throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBoolean(parameterIndex++, true);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setBoolean(boolean value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBoolean(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setInteger08(byte value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setByte(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setInteger16(short value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setShort(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setInteger32(int value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setInt(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setInteger64(long value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setLong(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setInteger(@Nonnull BigInteger value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBytes(parameterIndex++, value.toByteArray());
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setDecimal32(float value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setFloat(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setDecimal64(double value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setDouble(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setString01(char value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setString(parameterIndex++, String.valueOf(value));
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setString64(@Nonnull @MaxSize(64) String value) throws FailedSQLValueConversionException {
        Require.that(value.length() <= 64).orThrow("The length of the string is at most 64.");
        
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setString(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setString(@Nonnull String value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setString(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setBinary128(@Nonnull @Size(16) byte[] value) throws FailedSQLValueConversionException {
        Require.that(value.length == 16).orThrow("The length of the byte array is 16.");
        
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setBinary256(@Nonnull @Size(32) byte[] value) throws FailedSQLValueConversionException {
        Require.that(value.length == 32).orThrow("The length of the byte array is 32.");
        
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setBinary(@Nonnull byte[] value) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBytes(parameterIndex++, value);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    @Impure
    @Override
    public void setBinaryStream(@Nonnull InputStream stream, int length) throws FailedSQLValueConversionException {
        Require.that(Database.getInstance().supportsBinaryStreams()).orThrow("The database supports binary streams.");
        
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setBinaryStream(parameterIndex++, stream, length);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    // TODO: implement
    @Impure
    @Override
    public <T> void setArray(T[] value, Consumer<T> entityCollector) {
        
    }
    
    // TODO: implement
    @Impure
    @Override
    public <T> void setList(List<T> value, Consumer<T> entityCollector) {
        
    }
    
    // TODO: implement
    @Impure
    @Override
    public <T> void setSet(Set<T> value, Consumer<T> entityCollector) {
        
    }
    
    // TODO: implement
    @Impure
    @Override
    public <K, V> void setMap(Map<K, V> value, Consumer<K> genericTypeKey, Consumer<V> genericTypeValue) {
        
    }
    
    // TODO: implement
    @Impure
    @Override
    public void setNull() {
        
    }
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    @Impure
    @Override
    public void setNull(int typeCode) throws FailedSQLValueConversionException {
        try {
            int next = orderOfExecution.poll();
            preparedStatements.get(next).setNull(parameterIndex++, typeCode);
        } catch (@Nonnull SQLException exception) {
            throw FailedSQLValueConversionException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    @Impure
    @Override
    public void addBatch() throws FailedSQLValueConversionException {
        throw new UnsupportedOperationException("addBatch() is currently not supported");
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws FailedResourceClosingException {
        try {
            for (@Nonnull PreparedStatement preparedStatement : preparedStatements) {
                preparedStatement.close();
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedResourceClosingException.get(exception);
        }
    }
    
}
