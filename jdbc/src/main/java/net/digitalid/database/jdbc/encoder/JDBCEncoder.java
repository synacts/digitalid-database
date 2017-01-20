package net.digitalid.database.jdbc.encoder;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
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
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

/**
 * This classes uses the JDBC prepared statement to collect the values.
 */
public abstract class JDBCEncoder implements SQLEncoder {
    
    protected final @Nonnull PreparedStatement preparedStatement;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected JDBCEncoder(@Nonnull PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public @Nonnull Integer encodeNull(int typeCode) throws DatabaseException {
        return null;
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
    
    @Override
    @Pure
    public @Nonnull Representation getRepresentation() {
        return null;
    }
    
    // TODO: should this be done in the general SQL Data Manipulation Language Encoder class? In other words,
    // TODO: does it differ for each driver implementation?
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeObject(@Nonnull Converter<TYPE, ?> converter, @Nonnull @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        Require.that(object != null).orThrow("The given object, that should be encoded, may not be null.");
        // TODO: check if the object contains information that indicate that it should be stored in a different
        // TODO: table, or if the object should not be encoded at all, but instead only referenced (e.g. if the
        // TODO: object must pre-exist).
        return converter.convert(object, this);
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeNullableObject(@Nonnull Converter<TYPE, ?> converter, @Nullable @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        // TODO: do we actually need two? Is it not sufficient to check if it is null and act accordingly in that case?
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBoolean(boolean value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger08(byte value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger16(short value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger32(int value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger64(long value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeInteger(BigInteger value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeDecimal32(float value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeDecimal64(double value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString01(char value) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString64(String string) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeString(String string) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary128(@Nonnull @Size(16) byte[] bytes) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary256(@Nonnull @Size(32) byte[] bytes) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinary(@Nonnull byte[] bytes) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public void encodeBinaryStream(InputStream stream, int length) throws DatabaseException {
        
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeOrderedIterable(Converter<TYPE, ?> converter, FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeOrderedIterableWithNullableElements(Converter<TYPE, ?> converter, FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeUnorderedIterable(Converter<TYPE, ?> converter, FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> int encodeUnorderedIterableWithNullableElements(Converter<TYPE, ?> converter, FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> int encodeMap(Converter<KEY, ?> keyConverter, Converter<VALUE, ?> valueConverter, Map<@Nonnull KEY, @Nonnull VALUE> map) throws DatabaseException {
        return 0;
    }
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> int encodeMapWithNullableValues(Converter<KEY, ?> keyConverter, Converter<VALUE, ?> valueConverter, Map<@Nullable KEY, @Nullable VALUE> map) throws DatabaseException {
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
    public void startEncrypting(Cipher cipher) {
        
    }
    
    @Override
    @PureWithSideEffects
    public void stopEncrypting() throws DatabaseException {
        
    }
 
}
