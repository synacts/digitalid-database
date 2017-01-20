package net.digitalid.database.jdbc.encoder;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.zip.Deflater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
public abstract class JDBCEncoder implements SQLEncoder {
    
    protected final @Nonnull PreparedStatement preparedStatement;
    
    private int parameterIndex;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected JDBCEncoder(@Nonnull PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
        this.parameterIndex = 1;
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public void encodeNull(int typeCode) throws DatabaseException {
    }
    
    // TODO: can we remove this?
    @Override
    @Deprecated
    @PureWithSideEffects
    public void addBatch() throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void close() throws DatabaseException {
        
    }
    
    // TODO: Move to a superclass.
    @Override
    @Pure
    public @Nonnull Representation getRepresentation() {
        return Representation.INTERNAL;
    }
    
    // TODO: should this be done in the general SQL Data Manipulation Language Encoder class? In other words,
    // TODO: does it differ for each driver implementation?
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeObject(@Nonnull Converter<TYPE, ?> converter, @Nonnull @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        Require.that(object != null).orThrow("The given object, that should be encoded, may not be null.");
        converter.convert(object, this);
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeNullableObject(@Nonnull Converter<TYPE, ?> converter, @Nullable @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        if (object == null) {
            final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
            for (@Nonnull CustomField field : fields) {
                if (field.getCustomType().isObjectType()) {
                    encodeNullableObject(((CustomType.CustomConverterType) field.getCustomType()).getConverter(), null);
                } else {
                    if (!field.getCustomType().isCompositeType()) {
                        encodeNull(typeMapping.get(field.getCustomType()));
                    }
                }
            }
        } else {
            encodeObject(converter, object);
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBoolean(boolean value) throws DatabaseException {
        try {
            preparedStatement.setBoolean(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger08(byte value) throws DatabaseException {
        try {
            preparedStatement.setByte(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger16(short value) throws DatabaseException {
        try {
            preparedStatement.setShort(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger32(int value) throws DatabaseException {
        try {
            preparedStatement.setInt(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger64(long value) throws DatabaseException {
        try {
            preparedStatement.setLong(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger(@Nonnull BigInteger value) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, value.toByteArray());
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeDecimal32(float value) throws DatabaseException {
        try {
            preparedStatement.setFloat(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeDecimal64(double value) throws DatabaseException {
        try {
            preparedStatement.setDouble(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString01(char value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, String.valueOf(value));
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString64(@Nonnull @MaxSize(64) String value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString(@Nonnull String value) throws DatabaseException {
        try {
            preparedStatement.setString(parameterIndex++, value);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary128(@Nonnull @Size(16) byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary256(@Nonnull @Size(32) byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary(@Nonnull byte[] bytes) throws DatabaseException {
        try {
            preparedStatement.setBytes(parameterIndex++, bytes);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinaryStream(@Nonnull InputStream stream, int length) throws DatabaseException {
        try {
            preparedStatement.setBinaryStream(parameterIndex++, stream, length);
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeOrderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeOrderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeUnorderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeUnorderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> int encodeMap(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nonnull KEY, @Nonnull VALUE> map) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> int encodeMapWithNullableValues(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nullable KEY, @Nullable VALUE> map) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public boolean isHashing() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startHashing(MessageDigest digest) {
        
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull byte[] stopHashing() {
        return new byte[0];
    }
    
    @Override
    @PureWithSideEffects
    public boolean isCompressing() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startCompressing(Deflater deflater) {
        
    }
    
    @Override
    @PureWithSideEffects
    public void stopCompressing() throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public boolean isEncrypting() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startEncrypting(@Nonnull Cipher cipher) {
        
    }
    
    @Override
    @PureWithSideEffects
    public void stopEncrypting() throws DatabaseException {
        
    }
 
}
