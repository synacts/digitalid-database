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

import net.digitalid.utility.annotations.method.Impure;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLEncoderImplementation;

import android.database.sqlite.SQLiteDatabase;

/**
 * The Android encoder is used for collecting values for SQL statement using the Android SQLite database driver.
 */
public abstract class AndroidEncoder extends SQLEncoderImplementation {
    
    /* -------------------------------------------------- SQLite Database -------------------------------------------------- */
    
    /**
     * The sqlite database object, which is either a readable or a writable connection object
     * is used for execution of SQL statement on Android.
     */
    protected final @Nonnull SQLiteDatabase sqliteDatabase;
    
    /* -------------------------------------------------- Parameter Index -------------------------------------------------- */
    
    /**
     * The parameter index indicates at which position the next value is inserted. It is 0-indexed.
     */
    protected int parameterIndex = 0;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected AndroidEncoder(@Nonnull SQLiteDatabase sqliteDatabase) {
        this.sqliteDatabase = sqliteDatabase;
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException {
        sqliteDatabase.close();
    }
    
}
