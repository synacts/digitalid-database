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

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.android.decoder.AndroidDecoderBuilder;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidSelectEncoder extends AndroidWhereClauseEncoder implements SQLQueryEncoder {
    
    private final @Nonnull String query;
    
    protected AndroidSelectEncoder(@Nonnull SQLiteDatabase sqliteDatabase, @Nonnull String query, int sizeWhereArgs) {
        super(sqliteDatabase, null, sizeWhereArgs);
        this.query = query;
    }
    
    @Override
    @PureWithSideEffects
    public @Nonnull SQLDecoder execute() throws DatabaseException {
        final @Nonnull Cursor cursor = sqliteDatabase.rawQuery(query, whereArgs);
        return AndroidDecoderBuilder.withCursor(cursor).build();
    }
       
}
