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
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.functional.iterables.FiniteIterable;
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
public interface SQLEncoder extends Encoder<DatabaseException> {
    
    /* -------------------------------------------------- Representation -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Representation getRepresentation();
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
=     */
    @Impure
    public void encodeNull(int typeCode) throws DatabaseException;
    
    /**
     * Stores the mapping from custom types to SQL types.
     */
    static @Nonnull ImmutableMap<@Nonnull CustomType, @Nonnull Integer> TYPES = ImmutableMap
            .with(CustomType.BOOLEAN, Types.BOOLEAN)
            .with(CustomType.INTEGER08, Types.TINYINT)
            .with(CustomType.INTEGER16, Types.SMALLINT)
            .with(CustomType.INTEGER32, Types.INTEGER)
            .with(CustomType.INTEGER64, Types.BIGINT)
            .with(CustomType.INTEGER, Types.BLOB)
            .with(CustomType.DECIMAL32, Types.FLOAT)
            .with(CustomType.DECIMAL64, Types.DOUBLE)
            .with(CustomType.STRING1, Types.CHAR)
            .with(CustomType.STRING64, Types.VARCHAR)
            .with(CustomType.STRING128, Types.VARCHAR)
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
        final @Nullable Integer result = TYPES.get(customType);
        if (result == null) { throw CaseExceptionBuilder.withVariable("customType").withValue(customType).build(); }
        else { return result; }
    }
    
    /**
     * Sets the next parameter of the given custom type to null.
=     */
    @Impure
    public void encodeNull(@Nonnull CustomType customType) throws DatabaseException;
    
    // TODO: Why are all the following methods (and the getRepresentation() above) redeclared here?
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    @Impure
    @Override
    public <TYPE> void encodeObject(@Nonnull Converter<TYPE, ?> converter, @Nonnull @NonCaptured @Unmodified TYPE object) throws DatabaseException;
    
    @Impure
    @Override
    public <TYPE> void encodeNullableObject(@Nonnull Converter<TYPE, ?> converter, @Nullable @NonCaptured @Unmodified TYPE object) throws DatabaseException;
    
    /* -------------------------------------------------- Iterables -------------------------------------------------- */
    
    @Impure
    @Override
    public <TYPE> void encodeOrderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException;
    
    @Impure
    @Override
    public <TYPE> void encodeOrderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException;
    
    @Impure
    @Override
    public <TYPE> void encodeUnorderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException;
    
    @Impure
    @Override
    public <TYPE> void encodeUnorderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException;
    
    /* -------------------------------------------------- Maps -------------------------------------------------- */
    
    @Impure
    @Override
    public <KEY, VALUE> void encodeMap(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nonnull KEY, @Nonnull VALUE> map) throws DatabaseException;
    
    @Impure
    @Override
    public <KEY, VALUE> void encodeMapWithNullableValues(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nullable KEY, @Nullable VALUE> map) throws DatabaseException;
    
    /* -------------------------------------------------- Hashing -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isHashing();
    
    @Impure
    @Override
    public void startHashing(@Nonnull MessageDigest digest);
    
    @Impure
    @Override
    public @Nonnull byte[] stopHashing();
    
    /* -------------------------------------------------- Compression -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isCompressing();
    
    @Impure
    @Override
    public void startCompressing(@Nonnull Deflater deflater);
    
    @Impure
    @Override
    public void stopCompressing() throws DatabaseException;
    
    /* -------------------------------------------------- Encryption -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isEncrypting();
    
    @Impure
    @Override
    public void startEncrypting(@Nonnull Cipher cipher);
    
    @Impure
    @Override
    public void stopEncrypting() throws DatabaseException;
 
}
