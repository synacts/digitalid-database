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
package net.digitalid.database.interfaces;


import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Map;
import java.util.zip.Inflater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Shared;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.functional.failable.FailableCollector;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

/**
 * An SQL decoder decodes values from an SQL result set.
 * 
 * @see SQLEncoder
 */
@Mutable
public abstract class SQLDecoder implements Decoder<DatabaseException> {
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    /**
     * Moves the cursor to the next row of the selection result.
     * 
     * @return whether there was another row to which the cursor could be moved.
     */
    @Impure
    public abstract boolean moveToNextRow() throws DatabaseException;
    
    /**
     * Moves the cursor to the first row of the selection result.
     */
    @Impure
    public abstract boolean moveToFirstRow() throws DatabaseException;
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Returns whether the last returned column was null.
     */
    @Pure
    public abstract boolean wasNull() throws DatabaseException;
    
    /* -------------------------------------------------- Representation -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Representation getRepresentation() {
        return Representation.INTERNAL;
    }
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    @Pure
    @Override
    public <TYPE, PROVIDED> @Nonnull TYPE decodeObject(@Nonnull Converter<TYPE, PROVIDED> converter, @Shared PROVIDED provided) throws DatabaseException, RecoveryException {
        return converter.recover(this, provided);
    }
    
    @Pure
    @Override
    @TODO(task = "This is wrong. The converter expects non-null values. This method returns null when all columns are null and calls the decodeObject method otherwise.", date = "2017-01-25", author = Author.KASPAR_ETTER)
    public <TYPE, PROVIDED> @Nullable TYPE decodeNullableObject(@Nonnull Converter<TYPE, PROVIDED> converter, @Shared PROVIDED provided) throws DatabaseException, RecoveryException {
        try {
            return decodeObject(converter, provided);
        } catch (@Nonnull DatabaseException | RecoveryException exception) {
            if (wasNull()) { return null; } // TODO: This try catch code was written on 2017-08-19. Is this the best we can do?
            else { throw exception; }
        }
    }
    
    /* -------------------------------------------------- Decoding -------------------------------------------------- */
    
    @Pure
    protected void throwNullException() throws DatabaseException {
        throw DatabaseExceptionBuilder.withCause(new SQLException("Read null instead of a value.")).build();
    }
    
    /* -------------------------------------------------- Iterables -------------------------------------------------- */
    
    @Impure
    @Override
    public <TYPE, PROVIDED, ITERABLE, COLLECTOR extends FailableCollector<@Nonnull TYPE, @Nonnull ITERABLE, RecoveryException, RecoveryException>> @Nonnull ITERABLE decodeOrderedIterable(@Nonnull Converter<TYPE, PROVIDED> converter, @Nonnull @Shared PROVIDED provided, @Nonnull UnaryFunction<Integer, @Nonnull COLLECTOR> constructor) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode ordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE, PROVIDED, ITERABLE, COLLECTOR extends FailableCollector<@Nullable TYPE, @Nonnull ITERABLE, RecoveryException, RecoveryException>> @Nonnull ITERABLE decodeOrderedIterableWithNullableElements(@Nonnull Converter<TYPE, PROVIDED> converter, @Nonnull @Shared PROVIDED provided, @Nonnull UnaryFunction<Integer, @Nonnull COLLECTOR> constructor) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode ordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE, PROVIDED, ITERABLE, COLLECTOR extends FailableCollector<@Nonnull TYPE, @Nonnull ITERABLE, RecoveryException, RecoveryException>> @Nonnull ITERABLE decodeUnorderedIterable(@Nonnull Converter<TYPE, PROVIDED> converter, @Nonnull @Shared PROVIDED provided, UnaryFunction<Integer, @Nonnull COLLECTOR> constructor) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode unordered iterables.");
    }
    
    @Impure
    @Override
    public <TYPE, PROVIDED, ITERABLE, COLLECTOR extends FailableCollector<@Nullable TYPE, @Nonnull ITERABLE, RecoveryException, RecoveryException>> @Nonnull ITERABLE decodeUnorderedIterableWithNullableElements(@Nonnull Converter<TYPE, PROVIDED> converter, @Nonnull @Shared PROVIDED provided, UnaryFunction<Integer, @Nonnull COLLECTOR> constructor) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode unordered iterables.");
    }
    
    /* -------------------------------------------------- Maps -------------------------------------------------- */
    
    @Impure
    @Override
    public <KEY, PROVIDED_FOR_KEY, VALUE, PROVIDED_FOR_VALUE> Map<@Nonnull KEY, @Nonnull VALUE> decodeMap(@Nonnull Converter<KEY, PROVIDED_FOR_KEY> keyprovided_for_keyConverter, @Nonnull @Shared PROVIDED_FOR_KEY provided_for_key, @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> valueprovided_for_valueConverter, @Nonnull @Shared PROVIDED_FOR_VALUE provided_for_value, @Nonnull Map<@Nonnull KEY, @Nonnull VALUE> emptyMap) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode maps.");
    }
    
    @Impure
    @Override
    public <KEY, PROVIDED_FOR_KEY, VALUE, PROVIDED_FOR_VALUE> Map<@Nullable KEY, @Nullable VALUE> decodeMapWithNullableValues(@Nonnull Converter<KEY, PROVIDED_FOR_KEY> keyprovided_for_keyConverter, @Nonnull @Shared PROVIDED_FOR_KEY provided_for_key, @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> valueprovided_for_valueConverter, @Nonnull @Shared PROVIDED_FOR_VALUE provided_for_value, @Nonnull Map<@Nullable KEY, @Nullable VALUE> emptyMap) throws DatabaseException, RecoveryException {
        throw new UnsupportedOperationException("The SQL Decoder cannot decode maps.");
    }
    
    /* -------------------------------------------------- Hashing -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean isHashing() {
        return false;
    }
    
    @Impure
    @Override
    public void startHashing(@Nonnull MessageDigest digest) {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle hashes.");
    }
    
    @Impure
    @Override
    public @Nonnull byte[] stopHashing() {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle hashes.");
    }
    
    /* -------------------------------------------------- Decompression -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean isDecompressing() {
        return false;
    }
    
    @Impure
    @Override
    public void startDecompressing(@Nonnull Inflater inflater) {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle decompression.");
    }
    
    @Impure
    @Override
    public void stopDecompressing() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle decompression.");
    }
    
    /* -------------------------------------------------- Decryption -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean isDecrypting() {
        return false;
    }
    
    @Impure
    @Override
    public void startDecrypting(Cipher cipher) {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle decryption.");
    }
    
    @Impure
    @Override
    public void stopDecrypting() throws DatabaseException {
        throw new UnsupportedOperationException("The SQL Decoder cannot handle decryption.");
    }
    
}
