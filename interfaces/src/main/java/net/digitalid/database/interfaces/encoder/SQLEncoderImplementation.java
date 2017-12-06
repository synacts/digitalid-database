/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
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
public abstract class SQLEncoderImplementation implements SQLEncoder {
    
    /* -------------------------------------------------- Representation -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Representation getRepresentation() {
        return Representation.INTERNAL;
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
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
    private static @Nonnull ImmutableMap<@Nonnull CustomType, @Nonnull Integer> TYPES = ImmutableMap
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
    public void encodeNull(@Nonnull CustomType customType) throws DatabaseException {
        encodeNull(getSQLType(customType));
    }
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    @Impure
    @Override
    public <TYPE> void encodeObject(@Nonnull Converter<TYPE, ?> converter, @Nonnull @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        Require.that(object != null).orThrow("The given object, that should be encoded, may not be null.");
        converter.convert(object, this);
    }
    
    @Impure
    @Override
    public <TYPE> void encodeNullableObject(@Nonnull Converter<TYPE, ?> converter, @Nullable @NonCaptured @Unmodified TYPE object) throws DatabaseException {
        if (object == null) {
            final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
            for (@Nonnull CustomField field : fields) {
                if (field.getCustomType().isObjectType()) {
                    encodeNullableObject(((CustomType.CustomConverterType) field.getCustomType()).getConverter(), null);
                } else {
                    if (!field.getCustomType().isCompositeType()) {
                        encodeNull(TYPES.get(field.getCustomType()));
                    }
                }
            }
        } else {
            encodeObject(converter, object);
        }
    }
    
    /* -------------------------------------------------- Iterables -------------------------------------------------- */
    
    @Impure
    @Override
    public <TYPE> void encodeOrderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode ordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE> void encodeOrderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode ordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE> void encodeUnorderedIterable(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nonnull TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode unordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE> void encodeUnorderedIterableWithNullableElements(@Nonnull Converter<TYPE, ?> converter, @Nonnull FiniteIterable<@Nullable TYPE> iterable) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode unordered iterables.");
    }
    
    /* -------------------------------------------------- Maps -------------------------------------------------- */
    
    @Impure
    @Override
    public <KEY, VALUE> void encodeMap(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nonnull KEY, @Nonnull VALUE> map) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode maps.");
    }
    
    @Impure
    @Override
    public <KEY, VALUE> void encodeMapWithNullableValues(@Nonnull Converter<KEY, ?> keyConverter, @Nonnull Converter<VALUE, ?> valueConverter, @Nonnull Map<@Nullable KEY, @Nullable VALUE> map) throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot encode maps.");
    }
    
    /* -------------------------------------------------- Hashing -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isHashing() {
        return false;
    }
    
    @Impure
    @Override
    public void startHashing(@Nonnull MessageDigest digest) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle hashes.");
    }
    
    @Impure
    @Override
    public @Nonnull byte[] stopHashing() {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle hashes.");
    }
    
    /* -------------------------------------------------- Compression -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isCompressing() {
        return false;
    }
    
    @Impure
    @Override
    public void startCompressing(@Nonnull Deflater deflater) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle compression.");
    }
    
    @Impure
    @Override
    public void stopCompressing() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle compression.");
    }
    
    /* -------------------------------------------------- Encryption -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isEncrypting() {
        return false;
    }
    
    @Impure
    @Override
    public void startEncrypting(@Nonnull Cipher cipher) {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle encryption.");
    }
    
    @Impure
    @Override
    public void stopEncrypting() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Encoder cannot handle encryption.");
    }
 
}
