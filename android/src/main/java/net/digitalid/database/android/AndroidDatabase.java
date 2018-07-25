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
package net.digitalid.database.android;

import java.sql.ResultSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.logging.Caller;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Mutable;

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
import net.digitalid.database.dialect.statement.schema.SQLCreateSchemaStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class implements the database interface on Android.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public class AndroidDatabase extends Database {
    
    /* -------------------------------------------------- Helper -------------------------------------------------- */
    
    private final @Nonnull SQLiteOpenHelper helper;
    
    protected AndroidDatabase(@Nonnull Context context, @Nonnull String databaseName, int databaseVersion) {
        // The third parameter is the SQLite database cursor factory. If set to null, the default cursor factory is chosen.
        this.helper = new SQLiteOpenHelper(context, databaseName, null, databaseVersion) {
            
            @Impure
            @Override
            public void onCreate(@Nonnull SQLiteDatabase sqLiteDatabase) {
                // The creation of the tables is done via the execute method. No need to prepare something here.
            }
            
            @Impure
            @Override
            public void onUpgrade(@Nonnull SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                // The creation of the tables is done via the execute method. No need to prepare something here.
            }
            
        };
    }
    
    @Override
    @PureWithSideEffects
    public void close() {
        helper.close();
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Begins a new transaction.
     */
    @Impure
    @NonCommitting
    protected void begin() {
        if (!helper.getWritableDatabase().inTransaction()) {
            helper.getWritableDatabase().beginTransaction();
        }
    }
    
    @Impure
    @Override
    protected void commitTransaction() {
        helper.getWritableDatabase().setTransactionSuccessful();
        helper.getWritableDatabase().endTransaction();
        Log.debugging("Committed the database transaction from $ through $.", Caller.get(6).replace("net.digitalid.", ""), Caller.get(5).replace("net.digitalid.", ""));
        runRunnablesAfterCommit();
    }
    
    @Impure
    @Override
    protected void rollbackTransaction() {
        helper.getWritableDatabase().endTransaction();
        Log.debugging("Rolled back the database transaction from $ through $.", Caller.get(6).replace("net.digitalid.", ""), Caller.get(5).replace("net.digitalid.", ""));
        runRunnablesAfterRollback();
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    /**
     * Executes the given table statement on the given unit.
     */
    @PureWithSideEffects
    protected void executeStatement(@Nonnull SQLTableStatement tableStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull String statementString = SQLDialect.unparse(tableStatement, unit);
        Log.debugging("Executing $.", statementString);
        helper.getWritableDatabase().execSQL(statementString);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLCreateSchemaStatement createSchemaStatement, @Nonnull Unit unit) throws DatabaseException {
        throw new UnsupportedOperationException("Creating a new schema is not supported on Android.");
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(createTableStatement, unit);
    }
    
    @Override
    @PureWithSideEffects
    public void execute(@Nonnull SQLDropTableStatement dropTableStatement, @Nonnull Unit unit) throws DatabaseException {
        executeStatement(dropTableStatement, unit);
    }
    
    /* -------------------------------------------------- Encoders -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLInsertStatement insertStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull @NonEmpty @MaxSize(63) String tableName = insertStatement.getTable().getTable().getString();
        final @Nonnull @NonNullableElements String[] columnNames = insertStatement.getColumns().map(SQLIdentifier::getString).toArray(new String[0]);
        return AndroidInsertEncoderBuilder.withSqLiteDatabase(helper.getWritableDatabase()).withTableName(tableName).withColumnNames(columnNames).build();
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
    private @NonNegative int getNumberWhereClauseParameters(@Nullable String whereClauseString) {
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
        final @Nonnull AndroidWhereClauseEncoder whereClauseEncoder = AndroidWhereClauseEncoderBuilder.withSqliteDatabase(helper.getWritableDatabase()).withSizeWhereArgs(sizeWhereArgs).withWhereClause(whereClauseString).build();
        return AndroidUpdateEncoderBuilder.withSqLiteDatabase(helper.getWritableDatabase()).withTableName(tableName).withColumnNames(columnNames).withWhereClauseEncoder(whereClauseEncoder).build();
    }
    
    @Pure
    @Override
    public @Nonnull SQLActionEncoder getEncoder(@Nonnull SQLDeleteStatement deleteStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull @NonEmpty @MaxSize(63) String tableName = deleteStatement.getTable().getTable().getString();
        final @Nullable String whereClauseString = getWhereClauseString(deleteStatement.getWhereClause(), unit);
        final int sizeWhereArgs = getNumberWhereClauseParameters(whereClauseString);
        return AndroidDeleteEncoderBuilder.withSqliteDatabase(helper.getWritableDatabase()).withTableName(tableName).withSizeWhereArgs(sizeWhereArgs).build();
    }
    
    @Pure
    @Override
    public @Nonnull SQLQueryEncoder getEncoder(@Nonnull SQLSelectStatement selectStatement, @Nonnull Unit unit) throws DatabaseException {
        begin();
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        selectStatement.unparse(SQLDialect.instance.get(), unit, stringBuilder);
        final @Nullable String whereClauseString = getWhereClauseString(selectStatement, unit);
        final int sizeWhereArgs = getNumberWhereClauseParameters(whereClauseString);
        return AndroidSelectEncoderBuilder.withSqliteDatabase(helper.getWritableDatabase()).withQuery(stringBuilder.toString()).withSizeWhereArgs(sizeWhereArgs).build();
    }
    
    /* -------------------------------------------------- Testing -------------------------------------------------- */
    
    @Override
    @PureWithSideEffects
    public @Nonnull ResultSet executeQuery(@Nonnull @SQLStatement String query) throws DatabaseException {
        throw new UnsupportedOperationException("Database testing is not supported on Android.");
    }
    
}
