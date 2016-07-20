package net.digitalid.database.jdbc;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.functional.interfaces.Consumer;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.core.Database;
import net.digitalid.database.core.ExecutionData;
import net.digitalid.database.core.SQLType;
import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.processing.ParameterFunctionData;
import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.jdbc.preparedstatement.SQLStatementProcessingImplementation;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
public class JDBCValueCollector implements SQLValueCollector<Integer> {
    
    /**
     * The execution data object that is used to store the data that will be put into the prepared statement when the collection is ready.
     */
    private final @Nonnull ExecutionData<@Nonnull PreparedStatement> executionData;
    
    /**
     * Creates a new value collector with the given prepared statement.
     */
    private JDBCValueCollector(@Nonnull FiniteIterable<PreparedStatement> preparedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, @Nonnull ReadOnlyList<@Nonnull Integer> columnCountForGroup) {
        final @Nonnull FreezableArrayList<@Nonnull SQLStatementProcessingImplementation> rowsForStatementImplementations = FreezableArrayList.withElementsOf(preparedStatements.map(SQLStatementProcessingImplementation::with));
        this.executionData = ExecutionData.with(rowsForStatementImplementations, orderOfExecution, FreezableLinkedList.withElementsOf(columnCountForGroup));
    }
    
    /**
     * Returns a new value collector with the given prepared statement.
     */
    @Pure
    public static @Nonnull JDBCValueCollector get(@Nonnull FiniteIterable<PreparedStatement> preparedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, @Nonnull ReadOnlyList<@Nonnull Integer> columnCountForGroup) {
        return new JDBCValueCollector(preparedStatements, orderOfExecution, columnCountForGroup);
    }
    
    /* -------------------------------------------------- Setters -------------------------------------------------- */
    
    /**
     * The parameter function for boolean values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Boolean>, SQLException> setBooleanInPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Boolean object = triplet.getValue();
        
        preparedStatement.setBoolean(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setEmpty() throws FailedSQLValueConversionException {
        executionData.setColumnData(setBooleanInPreparedStatementFunction, true);
        return 1;
    }
    
    @Impure
    @Override
    public @Nonnull Integer setBoolean(boolean value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setBooleanInPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for integer 08 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Byte>, SQLException> setInteger08InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Byte object = triplet.getValue();
        
        preparedStatement.setByte(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setInteger08(byte value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setInteger08InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for integer 16 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Short>, SQLException> setInteger16InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Short object = triplet.getValue();
    
        preparedStatement.setShort(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setInteger16(short value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setInteger16InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for integer 32 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Integer>, SQLException> setInteger32InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Integer object = triplet.getValue();
    
        preparedStatement.setInt(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setInteger32(int value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setInteger32InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for integer 64 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Long>, SQLException> setInteger64InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Long object = triplet.getValue();
    
        preparedStatement.setLong(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setInteger64(long value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setInteger64InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for big integer values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull BigInteger>, SQLException> setIntegerInPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final @Nonnull int parameterIndex = triplet.getParameterIndex();
        final @Nonnull BigInteger object = triplet.getValue();
    
        preparedStatement.setBytes(parameterIndex, object.toByteArray());
    };
    
    @Impure
    @Override
    public @Nonnull Integer setInteger(@Nonnull BigInteger value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setIntegerInPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for decimal 32 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Float>, SQLException> setDecimal32InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Float object = triplet.getValue();
    
        preparedStatement.setFloat(parameterIndex, object);

    };
    
    @Impure
    @Override
    public @Nonnull Integer setDecimal32(float value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setDecimal32InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for decimal 64 values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Double>, SQLException> setDecimal64InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Double object = triplet.getValue();
    
        preparedStatement.setDouble(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setDecimal64(double value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setDecimal64InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for character values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Character>, SQLException> setString01InPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Character object = triplet.getValue();
    
        preparedStatement.setString(parameterIndex, String.valueOf(object));

    };
    
    @Impure
    @Override
    public @Nonnull Integer setString01(char value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setString01InPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for string values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull String>, SQLException> setStringInPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull String object = triplet.getValue();
    
        preparedStatement.setString(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setString64(@Nonnull @MaxSize(64) String value) throws FailedSQLValueConversionException {
        Require.that(value.length() <= 64).orThrow("The length of the string is at most 64.");
        
        executionData.setColumnData(setStringInPreparedStatementFunction, value);
        return 1;
    }
    
    @Impure
    @Override
    public @Nonnull Integer setString(@Nonnull String value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setStringInPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for byte array values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull byte[]>, SQLException> setBytesInPreparedStatementFunction = triplet -> {
        final PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull byte[] object = triplet.getValue();
    
        preparedStatement.setBytes(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setBinary128(@Nonnull @Size(16) byte[] value) throws FailedSQLValueConversionException {
        Require.that(value.length == 16).orThrow("The length of the byte array is 16.");
        
        executionData.setColumnData(setBytesInPreparedStatementFunction, value);
        return 1;
    }
    
    @Impure
    @Override
    public @Nonnull Integer setBinary256(@Nonnull @Size(32) byte[] value) throws FailedSQLValueConversionException {
        Require.that(value.length == 32).orThrow("The length of the byte array is 32.");
        
        executionData.setColumnData(setBytesInPreparedStatementFunction, value);
        return 1;
    }
    
    @Impure
    @Override
    public @Nonnull Integer setBinary(@Nonnull byte[] value) throws FailedSQLValueConversionException {
        executionData.setColumnData(setBytesInPreparedStatementFunction, value);
        return 1;
    }
    
    /**
     * The parameter function for binary stream values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull InputStream>, SQLException> setBinaryStreamInPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull InputStream object = triplet.getValue();
    
        preparedStatement.setBinaryStream(parameterIndex, object);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setBinaryStream(@Nonnull InputStream stream, int length) throws FailedSQLValueConversionException {
        Require.that(Database.getInstance().supportsBinaryStreams()).orThrow("The database supports binary streams.");
    
        executionData.setColumnData(setBinaryStreamInPreparedStatementFunction, stream);
        return 1;
    }
    
    // TODO: implement
    @Impure
    @Override
    public <T> @Nonnull Integer setArray(@Nonnull T[] value, @Nonnull Consumer<T> entityCollector) {
        return 0;
    }
    
    @Impure
    @Override
    public <T> @Nonnull Integer setList(@Nonnull List<@Nullable T> list, @Nonnull UnaryFunction<T, @Nonnull Integer> entityCollector) {
        executionData.multiplyRows(list.size());
        int insertedRows = executionData.getCurrentRowIndex();
        executionData.setRowIndex(insertedRows);
        
        for (int j = 0; j < list.size(); j++) {
            executionData.markPreparedStatementQueue();
            
            setInteger32(j);
            insertedRows += entityCollector.evaluate(list.get(j));
            
            executionData.resetPreparedStatementQueue();
            executionData.setRowIndex(insertedRows);
        }
        
        executionData.popPreparedStatementEntriesOfGroup();
        return list.size();
    }
    
    // TODO: implement
    @Impure
    @Override
    public <T> @Nonnull Integer setSet(@Nonnull Set<T> value, @Nonnull Consumer<T> entityCollector) {
        return 0;
    }
    
    // TODO: implement
    @Impure
    @Override
    public <K, V> @Nonnull Integer setMap(@Nonnull Map<K, V> value, @Nonnull Consumer<K> genericTypeKey, Consumer<V> genericTypeValue) {
        return 0;
    }
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * The parameter function for null values.
     */
    private final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, @Nonnull Integer>, SQLException> setNullWithTypeCodeInPreparedStatementFunction = triplet -> {
        final @Nonnull PreparedStatement preparedStatement = triplet.getPreparedStatement();
        final int parameterIndex = triplet.getParameterIndex();
        final @Nonnull Integer typeCode = triplet.getValue();
    
        preparedStatement.setNull(parameterIndex, typeCode);
    };
    
    @Impure
    @Override
    public @Nonnull Integer setNull(@Nonnull CustomType customType) {
        executionData.setColumnData(setNullWithTypeCodeInPreparedStatementFunction, SQLType.of(customType).getCode());
        return 1;
    }
    
    @Impure
    @Override
    public @Nonnull Integer setNull(int typeCode) throws FailedSQLValueConversionException {
        executionData.setColumnData(setNullWithTypeCodeInPreparedStatementFunction, typeCode);
        return 1;
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
            for (@Nonnull PreparedStatement preparedStatement : executionData.getPreparedStatements()) {
                preparedStatement.close();
            }
        } catch (@Nonnull SQLException exception) {
            throw FailedResourceClosingException.get(exception);
        }
    }
    
    @Pure
    public @Nonnull ReadOnlyList<PreparedStatement> getPreparedStatements() throws SQLException {
        return executionData.getPreparedStatements();
    }
    
}
