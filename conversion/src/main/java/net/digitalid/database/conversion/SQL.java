package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Shared;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.RecoveryExceptionBuilder;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.functional.iterables.InfiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.Table;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatementBuilder;
import net.digitalid.database.dialect.statement.insert.SQLConflictClause;
import net.digitalid.database.dialect.statement.insert.SQLExpressionsBuilder;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatementBuilder;
import net.digitalid.database.dialect.statement.insert.SQLRows;
import net.digitalid.database.dialect.statement.insert.SQLRowsBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatementBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLAllColumns;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLAllColumnsBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSource;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSourceBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatementBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatementBuilder;
import net.digitalid.database.dialect.statement.update.SQLAssignment;
import net.digitalid.database.dialect.statement.update.SQLAssignmentBuilder;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatement;
import net.digitalid.database.dialect.statement.update.SQLUpdateStatementBuilder;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;

/**
 * This class simplifies the execution of SQL statements.
 */
@Utility
public abstract class SQL {
    
    /* -------------------------------------------------- Configuration -------------------------------------------------- */
    
    /**
     * Stores a dummy configuration in order to have an initialization target for table creation.
     */
    public static final @Nonnull Configuration<Boolean> configuration = Configuration.with(Boolean.TRUE).addDependency(Database.instance);
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    /**
     * Creates a table for the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void createTable(@Nonnull Table<?, ?> table, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLUtility.getQualifiedTableName(table, unit);
        final SQLCreateTableStatementBuilder.@Nonnull InnerSQLCreateTableStatementBuilder sqlCreateTableStatementBuilder = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(SQLUtility.getColumnDeclarations(table));
        final @Nonnull ImmutableList<SQLTableConstraint> tableConstraints = SQLUtility.getTableConstraints(table, unit);
        if (!tableConstraints.isEmpty()) {
            sqlCreateTableStatementBuilder.withTableConstraints(tableConstraints);
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = sqlCreateTableStatementBuilder.build();
        Database.instance.get().execute(createTableStatement, unit);
        Database.commit();
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Drops the table of the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void dropTable(@Nonnull Table<?, ?> table, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull SQLQualifiedTable tableName = SQLUtility.getQualifiedTableName(table, unit);
        final @Nonnull SQLDropTableStatement dropTableStatement = SQLDropTableStatementBuilder.withTable(tableName).build();
        Database.instance.get().execute(dropTableStatement, unit);
        Database.commit();
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insert(@Nonnull Table<TYPE, ?> table, @Nonnull TYPE object, @Nonnull Unit unit, @Nonnull SQLConflictClause conflictClause) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLUtility.fillColumnNames(table, columns, "");
        
        final @Nonnull ImmutableList<@Nonnull SQLParameter> row = ImmutableList.withElementsOf(InfiniteIterable.repeat(SQLParameter.INSTANCE).limit(columns.size()));
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(SQLExpressionsBuilder.withExpressions(row).build())).build();
        
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLUtility.getQualifiedTableName(table, unit);
        final SQLInsertStatement insertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(ImmutableList.withElementsOf(columns)).withValues(rows).withConflictClause(conflictClause).build();
        
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(insertStatement, unit);
        actionEncoder.encodeObject(table, object);
        actionEncoder.execute();
    }
    
    /**
     * Inserts or aborts the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrAbort(@Nonnull Table<TYPE, ?> table, @Nonnull TYPE object, @Nonnull Unit unit) throws DatabaseException {
        insert(table, object, unit, SQLConflictClause.ABORT);
    }
    
    /**
     * Inserts or ignores the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrIgnore(@Nonnull Table<TYPE, ?> table, @Nonnull TYPE object, @Nonnull Unit unit) throws DatabaseException {
        insert(table, object, unit, SQLConflictClause.IGNORE);
    }
    
    /**
     * Inserts or replaces the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrReplace(@Nonnull Table<TYPE, ?> table, @Nonnull TYPE object, @Nonnull Unit unit) throws DatabaseException {
        insert(table, object, unit, SQLConflictClause.REPLACE);
    }
    
    /* -------------------------------------------------- Where -------------------------------------------------- */
    
    @Pure
    @NonCommitting
    private static @Nullable SQLBooleanExpression getWhereClause(@Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException {
        @Nullable SQLBooleanExpression whereClause = null;
        for (@Nonnull WhereCondition<?> whereCondition : whereConditions) {
            final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
            SQLUtility.fillColumnNames(whereCondition.getConverter(), columns, whereCondition.getPrefix());
            
            final @Nonnull FiniteIterable<@Nonnull SQLBooleanExpression> expressions = columns.map(column -> column.equal(SQLParameter.BOOLEAN));
            final @Nonnull SQLBooleanExpression expression = expressions.reduce((left, right) -> left.and(right));
            
            if (whereClause == null) { whereClause = expression; }
            else { whereClause = whereClause.and(expression); }
        }
        return whereClause;
    }
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Updates the columns of the given converter to the values of the given object with the given where condition in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable UPDATE_TYPE, @Unspecifiable WHERE_TYPE> void update(@Nonnull Table<UPDATE_TYPE, ?> updateTable, @Nonnull UPDATE_TYPE updateObject, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException {
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLUtility.getQualifiedTableName(updateTable, unit);
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLUtility.fillColumnNames(updateTable, columns, "");
        final @Nonnull FiniteIterable<SQLAssignment> assignments = columns.map(column -> SQLAssignmentBuilder.withColumn(column).withExpression(SQLParameter.INSTANCE).build());
        final SQLUpdateStatement updateStatement = SQLUpdateStatementBuilder.withTable(qualifiedTable).withAssignments(ImmutableList.withElementsOf(assignments)).withWhereClause(getWhereClause(whereConditions)).build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(updateStatement, unit);
        actionEncoder.encodeObject(updateTable, updateObject);
        for (@Nonnull WhereCondition<?> whereCondition : whereConditions) { whereCondition.encode(actionEncoder); }
        actionEncoder.execute();
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Deletes the entries of the given table with the given where conditions in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable WHERE_TYPE> void delete(@Nonnull Table<?, ?> deleteTable, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException {
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLUtility.getQualifiedTableName(deleteTable, unit);
        final SQLDeleteStatement deleteStatement = SQLDeleteStatementBuilder.withTable(qualifiedTable).withWhereClause(getWhereClause(whereConditions)).build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(deleteStatement, unit);
        for (@Nonnull WhereCondition<?> whereCondition : whereConditions) { whereCondition.encode(actionEncoder); }
        actionEncoder.execute();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    @NonCommitting
    @PureWithSideEffects
    private static @Capturable SQLDecoder getDecoder(@Nonnull Table<?, ?> selectTable, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException {
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLUtility.getQualifiedTableName(selectTable, unit);
        final @Nonnull ImmutableList<SQLAllColumns> columns = ImmutableList.withElements(SQLAllColumnsBuilder.buildWithTable(qualifiedTable));
        final @Nonnull ImmutableList<SQLTableSource> sources = ImmutableList.withElements(SQLTableSourceBuilder.withSource(qualifiedTable).build());
        final @Nonnull SQLSimpleSelectStatement selectStatement = SQLSimpleSelectStatementBuilder.withColumns(columns).withSources(sources).withWhereClause(getWhereClause(whereConditions)).build();
        
        final @Nonnull SQLQueryEncoder queryEncoder = Database.instance.get().getEncoder(selectStatement, unit);
        for (@Nonnull WhereCondition<?> whereCondition : whereConditions) { whereCondition.encode(queryEncoder); }
        return queryEncoder.execute();
    }
    
    /**
     * Returns the entries of the given table as a list of decoded objects with the given where conditions in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static @Capturable <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED> @Nonnull @NonNullableElements @NonFrozen FreezableList<SELECT_TYPE> selectAll(@Nonnull Table<SELECT_TYPE, PROVIDED> selectTable, @Shared PROVIDED provided, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException, RecoveryException {
        final @Nonnull SQLDecoder decoder = getDecoder(selectTable, unit, whereConditions);
        final @Nonnull FreezableArrayList<SELECT_TYPE> results = FreezableArrayList.withNoElements();
        if (decoder.moveToNextRow()) {
            do {
                results.add(selectTable.recover(decoder, provided));
            } while (decoder.moveToNextRow());
        }
        return results;
    }
    
    /**
     * Returns the first entry of the given table as a decoded object with the given where conditions in the given unit or null if there is no such entry.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED> @Nullable SELECT_TYPE selectFirst(@Nonnull Table<SELECT_TYPE, PROVIDED> selectTable, @Shared PROVIDED provided, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException, RecoveryException {
        final @Nonnull FreezableList<SELECT_TYPE> results = selectAll(selectTable, provided, unit, whereConditions);
        if (results.isEmpty()) { return null; } else { return results.getFirst(); }
    }
    
    /**
     * Returns the first entry of the given table as a decoded object with the given where conditions in the given unit or throws a {@link RecoveryException} if there is none.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED> @Nonnull SELECT_TYPE selectOne(@Nonnull Table<SELECT_TYPE, PROVIDED> selectTable, @Shared PROVIDED provided, @Nonnull Unit unit, @Nonnull @NonNullableElements WhereCondition<?>... whereConditions) throws DatabaseException, RecoveryException {
        final @Nullable SELECT_TYPE entry = selectFirst(selectTable, provided, unit, whereConditions);
        if (entry == null) {
            throw RecoveryExceptionBuilder.withMessage("There exists no entry in " +
                    "'" + selectTable.getTypeName() + "' " +
                    "of the unit '" + unit.getName() + "'" +
                    FiniteIterable.of(whereConditions).map(
                            (whereCondition) -> 
                                    "'" + whereCondition.getConverter().getTypeName() + "'" +
                                            (whereCondition.getPrefix().isEmpty() ? "" : " of '" + whereCondition.getPrefix() + "'") +
                                            " is '" + whereCondition.getObject() + "'")
                            .join(" where ", "", "") + ".")
                    .build();
        }
        else { return entry; }
    }
    
}
