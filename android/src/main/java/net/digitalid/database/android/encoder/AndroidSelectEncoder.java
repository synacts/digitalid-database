package net.digitalid.database.android.encoder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.android.decoder.AndroidDecoderBuilder;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
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
