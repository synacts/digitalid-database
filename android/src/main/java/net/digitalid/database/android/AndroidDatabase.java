package net.digitalid.database.android;

import java.sql.ResultSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.NonEmpty;

import net.digitalid.database.android.encoder.AndroidDeleteEncoderBuilder;
import net.digitalid.database.android.encoder.AndroidInsertEncoderBuilder;
import net.digitalid.database.android.encoder.AndroidSelectEncoderBuilder;
import net.digitalid.database.android.encoder.AndroidUpdateEncoderBuilder;
import net.digitalid.database.android.encoder.AndroidWhereClauseEncoder;
import net.digitalid.database.android.encoder.AndroidWhereClauseEncoderBuilder;
import net.digitalid.database.annotations.sql.SQLStatement;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.unit.Unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class implements the database interface on Android.
 */
@GenerateBuilder
@GenerateSubclass
public class AndroidDatabase extends SQLiteOpenHelper implements Database {
    
    protected AndroidDatabase(@Nonnull Context context, @Nonnull String databaseName, int databaseVersion) {
        // The third parameter is the SQLite database cursor factory. If set to null, the default cursor factory is chosen.
        super(context, databaseName, null, databaseVersion);
    }
    
    @Impure
    @Override
    public void onCreate(@Nonnull SQLiteDatabase sqLiteDatabase) {
        // The creation of the tables is done via the execute method. No need to prepare something here.
    }
    
    @Impure
    @Override
    public void onUpgrade(@Nonnull SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // The creation of the tables is done via the execute method. No need to prepare something here.
        // TODO: do we need to remember if there was a difference in the version?
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Begins a new transaction.
     */
    @Impure
    @NonCommitting
    protected void begin() throws DatabaseException {
        if (!getWritableDatabase().inTransaction()) {
            getWritableDatabase().beginTransaction();
        }
    }
    
    @Impure
    @Override
    public void commit() throws DatabaseException {
        getWritableDatabase().setTransactionSuccessful();
        getWritableDatabase().endTransaction();
    }
    
    @Impure
    @Override
    public void rollback() {
        getWritableDatabase().endTransaction();
    }
    
    @Impure
    private void executeStatement(@Nonnull SQLTableStatement tableStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        tableStatement.unparse(SQLDialect.instance.get(), unit, stringBuilder);
        getWritableDatabase().execSQL(stringBuilder.toString());
    }
    
    @Impure
    @Override
    public void execute(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(createTableStatement, unit);
    }
    
    @Impure
    @Override
    public void execute(@Nonnull SQLDropTableStatement dropTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(dropTableStatement, unit);
    }
    
    @Pure
    @Override
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLInsertStatement insertStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull @NonEmpty @MaxSize(63) String tableName = insertStatement.getTable().getTable().getString();
        final @Nonnull @NonNullableElements String[] columnNames = insertStatement.getColumns().map(SQLIdentifier::getString).toArray(new String[0]);
        return AndroidInsertEncoderBuilder.withSqLiteDatabase(getWritableDatabase()).withTableName(tableName).withColumnNames(columnNames).build();
    }
    
    @Pure
    private @Nullable String getWhereClauseString(@Nullable SQLBooleanExpression whereClause, @Nonnull Unit unit) {
        @Nullable String whereClauseString = null;
        if (whereClause != null) {
            final @Nonnull StringBuilder stringBuilder = new StringBuilder();
            whereClause.unparse(SQLDialect.instance.get(), unit, stringBuilder);
            whereClauseString = stringBuilder.toString();
        }
        return whereClauseString;
    }
    
    @Pure
    private int getNumberWhereClauseParameters(@Nullable String whereClauseString) {
        int sizeWhereArgs = 0;
        if (whereClauseString != null) {
            for (char c : whereClauseString.toCharArray()) {
                if (c == '?') {
                    sizeWhereArgs++;
                }
            }
        }
        return sizeWhereArgs;
    }
    
    @Pure
    @Override
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLUpdateStatement updateStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull @NonEmpty @MaxSize(63) String tableName = updateStatement.getTable().getTable().getString();
        final @Nonnull @NonNullableElements String[] columnNames = updateStatement.getAssignments().map(assignment -> assignment.getColumn().getString()).toArray(new String[0]);
        final @Nullable SQLBooleanExpression whereClause = updateStatement.getWhereClause();
        final @Nullable String whereClauseString = getWhereClauseString(whereClause, unit);
        final int sizeWhereArgs = getNumberWhereClauseParameters(whereClauseString);
        final @Nonnull AndroidWhereClauseEncoder whereClauseEncoder = AndroidWhereClauseEncoderBuilder.withSqliteDatabase(getWritableDatabase()).withSizeWhereArgs(sizeWhereArgs).withWhereClause(whereClauseString).build();
        return AndroidUpdateEncoderBuilder.withSqLiteDatabase(getWritableDatabase()).withTableName(tableName).withColumnNames(columnNames).withWhereClauseEncoder(whereClauseEncoder).build();
    }
    
    @Pure
    @Override
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLDeleteStatement deleteStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull @NonEmpty @MaxSize(63) String tableName = deleteStatement.getTable().getTable().getString();
        final @Nullable String whereClauseString = getWhereClauseString(deleteStatement.getWhereClause(), unit);
        final int sizeWhereArgs = getNumberWhereClauseParameters(whereClauseString);
        return AndroidDeleteEncoderBuilder.withSqliteDatabase(getWritableDatabase()).withTableName(tableName).withSizeWhereArgs(sizeWhereArgs).build();
    }
    
    @Pure
    @Override
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull SQLSelectStatement selectStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        selectStatement.unparse(SQLDialect.instance.get(), unit, stringBuilder);
        final @Nullable String whereClauseString = getWhereClauseString(selectStatement, unit);
        final int sizeWhereArgs = getNumberWhereClauseParameters(whereClauseString);
        return AndroidSelectEncoderBuilder.withSqliteDatabase(getWritableDatabase()).withQuery(stringBuilder.toString()).withSizeWhereArgs(sizeWhereArgs).build();
    }
    
    /* -------------------------------------------------- Testing -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public @Nonnull ResultSet executeQuery(@Nonnull @SQLStatement String query) throws DatabaseException {
        throw new UnsupportedOperationException("Database testing is not supported on Android.");
    }
    
}
