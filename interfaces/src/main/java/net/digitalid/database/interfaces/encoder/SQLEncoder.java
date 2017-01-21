package net.digitalid.database.interfaces.encoder;

import java.security.MessageDigest;
import java.sql.Types;
import java.util.Map;
import java.util.zip.Deflater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.immutable.ImmutableMap;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

/**
 * An SQL encoder encodes values to an SQL statement.
 * The task of the SQL encoder is to insert parameters into an executable parameterized statement, e.g.
 * a prepared statement in case of JDBC and a content values collection in case of an Android connection.
 * 
 * The SQL encoder cannot handle certain types of fields, such as iterables and maps. Support for 
 * iterables and maps would mean that a separate table must be created in which the iterable values, or map key value pairs,
 * are stored and which points to the table entry that the encoder prepares. Since those cases can be handled
 * by extensible properties or indexed properties, we can neglect the implementation of the encoding of iterable or map fields
 * for now.
 * 
 * Further, hashing, compression and encryption are currently not supported, because of the following arguments:
 * - Hashing is only considered useful if the integrity of the message is endangered, e.g. if a message is sent over a network channel.
 *   When communicating with the database, we can safely assume that this is not the case (otherwise the database link would take
 *   care of it.)
 * - Compression is not applicable on a SQL table entry. It is a task left to the database.
 * - Encryption is currently not implemented, but could be considered in the future for single-cell encryption.
 * 
 * @see SQLDecoder
 */
@Mutable
@TODO(task = "Check ultimately whether it makes sense that the SQLEncoder extends AutoCloseable.", date = "2017-01-19", author = Author.KASPAR_ETTER)
public abstract class SQLEncoder implements AutoCloseable, Encoder<DatabaseException> {
    
    /* -------------------------------------------------- Representation -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Representation getRepresentation() {
        return Representation.INTERNAL;
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public abstract void close() throws DatabaseException;
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
=     */
    @Impure
    public abstract void encodeNull(int typeCode) throws DatabaseException;
    
    /**
     * Stores the mapping from custom types to SQL types.
     */
    public static @Nonnull ImmutableMap<@Nonnull CustomType, @Nonnull Integer> typeMapping = ImmutableMap
            .with(CustomType.BOOLEAN, Types.BOOLEAN)
            .with(CustomType.INTEGER08, Types.TINYINT)
            .with(CustomType.INTEGER16, Types.SMALLINT)
            .with(CustomType.INTEGER32, Types.INTEGER)
            .with(CustomType.INTEGER64, Types.BIGINT)
            .with(CustomType.INTEGER, Types.BLOB)
            .with(CustomType.DECIMAL32, Types.FLOAT)
            .with(CustomType.DECIMAL64, Types.DOUBLE)
            .with(CustomType.STRING01, Types.CHAR)
            .with(CustomType.STRING64, Types.VARCHAR)
            .with(CustomType.STRING, Types.VARCHAR)
            .with(CustomType.BINARY128, Types.BINARY)
            .with(CustomType.BINARY256, Types.BINARY)
            .with(CustomType.BINARY, Types.BLOB)
            .build();
    
    /**
     * Returns the SQL type that corresponds to the given custom type.
     */
    @Pure
    public static int getSQLType(@Nonnull CustomType customType) {
        final @Nullable Integer result = typeMapping.get(customType);
        if (result == null) { throw CaseExceptionBuilder.withVariable("customType").withValue(customType).build(); }
        else { return result; }
    }
    
    /**
     * Sets the next parameter of the given custom type to null.
=     */
    @Impure
    public void encodeNull(@Nonnull CustomType customType) throws DatabaseException {
        encodeNull(getSQLType(customType));
    }
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
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
    
    /* -------------------------------------------------- Iterables -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeOrderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode ordered iterables.");
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeOrderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode ordered iterables.");
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeUnorderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode unordered iterables.");
    }
    
    @Override
    @PureWithSideEffects
    public <TYPE> void encodeUnorderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode unordered iterables.");
    }
    
    /* -------------------------------------------------- Maps -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> void encodeMap(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nonnull KEY, @Nonnull VALUE> map) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode maps.");
    }
    
    @Override
    @PureWithSideEffects
    public <KEY, VALUE> void encodeMapWithNullableValues(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nullable KEY, @Nullable VALUE> map) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode maps.");
    }
    
    /* -------------------------------------------------- Hashing -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public boolean isHashing() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startHashing(@Nonnull MessageDigest digest) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle hashes.");
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull byte[] stopHashing() {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle hashes.");
    }
    
    /* -------------------------------------------------- Compression -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public boolean isCompressing() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startCompressing(@Nonnull Deflater deflater) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle compression.");
    }
    
    @Override
    @PureWithSideEffects
    public void stopCompressing() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle compression.");
    }
    
    /* -------------------------------------------------- Encryption -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public boolean isEncrypting() {
        return false;
    }
    
    @Override
    @PureWithSideEffects
    public void startEncrypting(@Nonnull Cipher cipher) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle encryption.");
    }
    
    @Override
    @PureWithSideEffects
    public void stopEncrypting() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle encryption.");
    }
 
}
