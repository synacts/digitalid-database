package net.digitalid.database.android.encoder;

import javax.annotation.Nonnull;

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
public abstract class AndroidInsertEncoder extends AndroidInsertUpdateEncoder implements SQLActionEncoder {
    
    protected AndroidInsertEncoder(@Nonnull SQLiteDatabase sqLiteDatabase, @Nonnull String tableName, @Nonnull String[] columnNames) {
        super(sqLiteDatabase, tableName, columnNames);
    }
    
    @Override
    @PureWithSideEffects
    public void execute() throws DatabaseException {
        sqliteDatabase.insert(tableName, null, contentValues);
    }
    
}
