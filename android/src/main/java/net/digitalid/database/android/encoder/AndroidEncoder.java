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
