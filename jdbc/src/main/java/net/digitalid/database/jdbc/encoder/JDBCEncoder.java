package net.digitalid.database.jdbc.encoder;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.encoder.SQLEncoderImplementation;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
@GenerateBuilder
@GenerateSubclass
public abstract class JDBCEncoder extends SQLEncoderImplementation {
    
    /**
     * The prepared statement collects parameter values for the SQL statement.
     */
    protected final @Nonnull PreparedStatement preparedStatement;
    
    /**
     * The parameter index tracks at which position we are inserting the parameter. It is 1-indexed.
     */
    private int parameterIndex = 1;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Builds a new JDBC encoder based on a prepared statement object.
     */
    protected JDBCEncoder(@Nonnull PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Impure
    @Override
    public void encodeNull(int typeCode) throws DatabaseException {
        try {
            preparedStatement.setNull(parameterIndex++, typeCode);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void close() throws DatabaseException {
        try {
            preparedStatement.close();
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeBoolean(boolean value) throws DatabaseException {
        try {
            preparedStatement.setBoolean(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeInteger08(byte value) throws DatabaseException {
        try {
            preparedStatement.setByte(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeInteger16(short value) throws DatabaseException {
        try {
            preparedStatement.setShort(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeInteger32(int value) throws DatabaseException {
        try {
            preparedStatement.setInt(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeInteger64(long value) throws DatabaseException {
        try {
            preparedStatement.setLong(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeInteger(@Nonnull BigInteger value) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, value.toByteArray());
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeDecimal32(float value) throws DatabaseException {
        try {
            preparedStatement.setFloat(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeDecimal64(double value) throws DatabaseException {
        try {
            preparedStatement.setDouble(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeString01(char value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, String.valueOf(value));
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeString64(@Nonnull @MaxSize(64) String value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeString(@Nonnull String value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeBinary128(@Nonnull @Size(16) byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeBinary256(@Nonnull @Size(32) byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeBinary(@Nonnull byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Impure
    @Override
    public void encodeBinaryStream(@Nonnull InputStream stream, int length) throws DatabaseException {
        try {
            preparedStatement.setBinaryStream(parameterIndex++, stream, length);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    
}
