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
package net.digitalid.database.android.decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.contracts.Ensure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

import android.database.Cursor;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidDecoder extends SQLDecoder {
    
    private final @Nonnull Cursor cursor;
    
    private int columnIndex;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected AndroidDecoder(@Nonnull Cursor cursor) {
        this.cursor = cursor;
        this.columnIndex = 0;
    }
    
    /* -------------------------------------------------- Move -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean moveToNextRow() throws DatabaseException {
        final boolean hasNextRow = cursor.moveToNext();
        columnIndex = 0;
        return hasNextRow;
    }
    
    @Impure
    @Override
    public boolean moveToFirstRow() throws DatabaseException {
        return cursor.moveToFirst();
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException {
        cursor.close();
    }
    
    /* -------------------------------------------------- Nullness -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean wasNull() throws DatabaseException {
        return cursor.isNull(columnIndex - 1);
    }
    
    /* -------------------------------------------------- Decoding -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean decodeBoolean() throws DatabaseException {
        final boolean value = cursor.getShort(columnIndex++) != 0;
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public byte decodeInteger08() throws DatabaseException {
        final short number = cursor.getShort(columnIndex++);
        if (wasNull()) { throwNullException(); }
        Ensure.that(number <= Math.pow(2, 8)).orThrow("The number is not byte");
        return (byte) number;
    }
    
    @Impure
    @Override
    public short decodeInteger16() throws DatabaseException {
        final short value = cursor.getShort(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public int decodeInteger32() throws DatabaseException {
        final int value = cursor.getInt(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public long decodeInteger64() throws DatabaseException {
        final long value = cursor.getLong(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public @Nonnull BigInteger decodeInteger() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return new BigInteger(bytes);
    }
    
    @Impure
    @Override
    public float decodeDecimal32() throws DatabaseException {
        final float value = cursor.getFloat(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public double decodeDecimal64() throws DatabaseException {
        final double value = cursor.getDouble(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public char decodeString01() throws DatabaseException {
        final @Nonnull String character = cursor.getString(columnIndex++);
        if (wasNull()) { throwNullException(); }
        Ensure.that(character.length() == 1).orThrow("The value is not a character.");
        return character.charAt(0);
    }
    
    @Impure
    @Override
    public @Nonnull String decodeString64() throws DatabaseException {
        final @Nonnull String string = cursor.getString(columnIndex++);
        if (wasNull()) { throwNullException(); }
        Ensure.that(string.length() <= 64).orThrow("The value is not a string of length <= 64.");
        return string;
    }
    
    @Impure
    @Override
    public @Nonnull String decodeString() throws DatabaseException {
        final @Nonnull String value = cursor.getString(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public @Nonnull @Size(16) byte[] decodeBinary128() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        if (wasNull()) { throwNullException(); }
        Ensure.that(bytes.length <= 16).orThrow("The value is not a byte array of length <= 16.");
        return bytes;
    }
    
    @Impure
    @Override
    public @Nonnull @Size(32) byte[] decodeBinary256() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        if (wasNull()) { throwNullException(); }
        Ensure.that(bytes.length <= 32).orThrow("The value is not a byte array of length <= 32.");
        return bytes;
    }
    
    @Impure
    @Override
    public @Nonnull byte[] decodeBinary() throws DatabaseException {
        final @Nonnull byte[] value = cursor.getBlob(columnIndex++);
        if (wasNull()) { throwNullException(); }
        return value;
    }
    
    @Impure
    @Override
    public @Nonnull InputStream decodeBinaryStream() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        if (wasNull()) { throwNullException(); }
        final @Nonnull ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return stream;
    }
    
}
