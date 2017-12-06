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
package net.digitalid.database.android.encoder;

import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidWhereClauseEncoder extends AndroidEncoder implements SQLEncoder {
    
    protected final @Nullable String whereClause;
    
    protected final @Nonnull String[] whereArgs;
    
    protected AndroidWhereClauseEncoder(@Nonnull SQLiteDatabase sqliteDatabase, @Nullable String whereClause, int sizeWhereArgs) {
        super(sqliteDatabase);
        this.whereClause = whereClause;
        this.whereArgs = new String[sizeWhereArgs];
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Impure
    @Override
    public void encodeBoolean(boolean value) throws DatabaseException {
        whereArgs[parameterIndex++] = String.valueOf(value);
    }
    
    @Impure
    @Override
    public void encodeInteger08(byte value) throws DatabaseException {
        whereArgs[parameterIndex++] = Byte.toString(value);
    }
    
    @Impure
    @Override
    public void encodeInteger16(short value) throws DatabaseException {
        whereArgs[parameterIndex++] = Short.toString(value);
    }
    
    @Impure
    @Override
    public void encodeInteger32(int value) throws DatabaseException {
        whereArgs[parameterIndex++] = Integer.toString(value);
    }
    
    @Impure
    @Override
    public void encodeInteger64(long value) throws DatabaseException {
        whereArgs[parameterIndex++] = Long.toString(value);
    }
    
    @Impure
    @Override
    public void encodeInteger(@Nonnull BigInteger value) throws DatabaseException {
        whereArgs[parameterIndex++] = new String(value.toByteArray());
    }
    
    @Impure
    @Override
    public void encodeDecimal32(float value) throws DatabaseException {
        whereArgs[parameterIndex++] = Float.toString(value);
    }
    
    @Impure
    @Override
    public void encodeDecimal64(double value) throws DatabaseException {
        whereArgs[parameterIndex++] = Double.toString(value);
    }
    
    @Impure
    @Override
    public void encodeString01(char value) throws DatabaseException {
        whereArgs[parameterIndex++] = String.valueOf(value);
    }
    
    @Impure
    @Override
    public void encodeString64(@Nonnull String value) throws DatabaseException {
        whereArgs[parameterIndex++] = value;
    }
    
    @Impure
    @Override
    public void encodeString(@Nonnull String value) throws DatabaseException {
        whereArgs[parameterIndex++] = value;
    }
    
    @Impure
    @Override
    public void encodeBinary128(@Nonnull @Size(16) byte[] bytes) throws DatabaseException {
        throw new UnsupportedOperationException("Cannot use binary 128 values in the WHERE clause of the SQL statement");
    }
    
    @Impure
    @Override
    public void encodeBinary256(@Nonnull @Size(32) byte[] bytes) throws DatabaseException {
        throw new UnsupportedOperationException("Cannot use binary 256 values in the WHERE clause of the SQL statement");
    }
    
    @Impure
    @Override
    public void encodeBinary(@Nonnull byte[] bytes) throws DatabaseException {
        throw new UnsupportedOperationException("Cannot use binary values in the WHERE clause of the SQL statement");
    }
    
    @Impure
    @Override
    public void encodeBinaryStream(InputStream stream, int length) throws DatabaseException {
        throw new UnsupportedOperationException("Cannot use binary stream values in the WHERE clause of the SQL statement");
    }
    
    @Impure
    @Override
    public void encodeNull(int typeCode) throws DatabaseException {
        whereArgs[parameterIndex++] = null;
    }
    
}
