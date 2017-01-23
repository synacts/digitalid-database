package net.digitalid.database.android.encoder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidDeleteEncoder extends AndroidWhereClauseEncoder implements SQLActionEncoder {
    
    private final @Nonnull String tableName;
    
    protected AndroidDeleteEncoder(@Nonnull SQLiteDatabase sqliteDatabase, @Nonnull String tableName, @Nullable String whereClause, int sizeWhereArgs) {
        super(sqliteDatabase, whereClause, sizeWhereArgs);
        this.tableName = tableName;
    }
    
    @Override
    @PureWithSideEffects
    public void execute() throws DatabaseException {
        sqliteDatabase.delete(tableName, whereClause, whereArgs);
    }
       
}
