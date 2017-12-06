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

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidUpdateEncoder extends AndroidInsertUpdateEncoder implements SQLActionEncoder {
    
    private final @Nonnull AndroidWhereClauseEncoder androidWhereClauseEncoder;
    
    private final int columnsAmount;
    
    protected AndroidUpdateEncoder(@Nonnull SQLiteDatabase sqLiteDatabase, @Nonnull String tableName, @Nonnull String[] columnNames, @Nonnull AndroidWhereClauseEncoder whereClauseEncoder) {
        super(sqLiteDatabase, tableName, columnNames);
        this.androidWhereClauseEncoder = whereClauseEncoder;
        this.columnsAmount = columnNames.length;
    }
    
    @Override
    @PureWithSideEffects
    public void execute() throws DatabaseException {
        sqliteDatabase.update(tableName, contentValues, androidWhereClauseEncoder.whereClause, androidWhereClauseEncoder.whereArgs);
    }
    
    @Impure
    @Override
    public void encodeNull(int typeCode) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeNull(typeCode);
        } else {
            androidWhereClauseEncoder.encodeNull(typeCode);
        }
    }
    
    @Impure
    @Override
    public void encodeBoolean(boolean value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeBoolean(value);
        } else {
            androidWhereClauseEncoder.encodeBoolean(value);
        }
    }
    
    @Impure
    @Override
    public void encodeInteger08(byte value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeInteger08(value);
        } else {
            androidWhereClauseEncoder.encodeInteger08(value);
        }
    }
    
    @Impure
    @Override
    public void encodeInteger16(short value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeInteger16(value);
        } else {
            androidWhereClauseEncoder.encodeInteger16(value);
        }
    }
    
    @Impure
    @Override
    public void encodeInteger32(int value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeInteger32(value);
        } else {
            androidWhereClauseEncoder.encodeInteger32(value);
        }
    }
    
    @Impure
    @Override
    public void encodeInteger64(long value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeInteger64(value);
        } else {
            androidWhereClauseEncoder.encodeInteger64(value);
        }
    }
    
    @Impure
    @Override
    public void encodeInteger(@Nonnull BigInteger value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeInteger(value);
        } else {
            androidWhereClauseEncoder.encodeInteger(value);
        }
    }
    
    @Impure
    @Override
    public void encodeDecimal32(float value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeDecimal32(value);
        } else {
            androidWhereClauseEncoder.encodeDecimal32(value);
        }
    }
    
    @Impure
    @Override
    public void encodeDecimal64(double value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeDecimal64(value);
        } else {
            androidWhereClauseEncoder.encodeDecimal64(value);
        }
    }
    
    @Impure
    @Override
    public void encodeString01(char value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeString01(value);
        } else {
            androidWhereClauseEncoder.encodeString01(value);
        }
    }
    
    @Impure
    @Override
    public void encodeString64(@Nonnull @MaxSize(64) String value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeString64(value);
        } else {
            androidWhereClauseEncoder.encodeString64(value);
        }
    }
    
    @Impure
    @Override
    public void encodeString(@Nonnull String value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeString(value);
        } else {
            androidWhereClauseEncoder.encodeString(value);
        }
    }
    
    @Impure
    @Override
    public void encodeBinary128(@Nonnull @Size(16) byte[] value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeBinary128(value);
        } else {
            androidWhereClauseEncoder.encodeBinary128(value);
        }
    }
    
    @Impure
    @Override
    public void encodeBinary256(@Nonnull @Size(32) byte[] value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeBinary256(value);
        } else {
            androidWhereClauseEncoder.encodeBinary256(value);
        }
    }
    
    @Impure
    @Override
    public void encodeBinary(@Nonnull byte[] value) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeBinary(value);
        } else {
            androidWhereClauseEncoder.encodeBinary(value);
        }
    }
    
    @Impure
    @Override
    public void encodeBinaryStream(@Nonnull InputStream stream, int length) throws DatabaseException {
        if (parameterIndex < columnsAmount) {
            super.encodeBinaryStream(stream, length);
        } else {
            androidWhereClauseEncoder.encodeBinaryStream(stream, length);
        }
    }
    
}
