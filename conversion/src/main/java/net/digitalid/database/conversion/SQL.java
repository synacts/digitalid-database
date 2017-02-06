package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Shared;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.functional.iterables.InfiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.utility.SQLConversionUtility;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatementBuilder;
import net.digitalid.database.dialect.statement.insert.SQLExpressionsBuilder;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatementBuilder;
import net.digitalid.database.dialect.statement.insert.SQLRows;
import net.digitalid.database.dialect.statement.insert.SQLRowsBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
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
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.unit.Unit;

/**
 * This class simplifies the execution of SQL statements.
 */
@Utility
public abstract class SQL {
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    /**
     * Creates a table for the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void createTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnDeclarations(converter, columnDeclarations, false, "");
        final @Nonnull SQLQualifiedTable tableName = SQLExplicitlyQualifiedTableBuilder.withTable(SQLTableNameBuilder.withString(converter.getTypeName()).build()).withSchema(SQLSchemaNameBuilder.withString(unit.getName()).build()).build();
        SQLCreateTableStatementBuilder.@Nonnull InnerSQLCreateTableStatementBuilder sqlCreateTableStatementBuilder = SQLCreateTableStatementBuilder.withTable(tableName).withColumnDeclarations(ImmutableList.withElementsOf(columnDeclarations));
        // TODO: what if the referenced table is in another unit?
        final @Nonnull ImmutableList<SQLTableConstraint> tableConstraints = SQLConversionUtility.getTableConstraints(converter, unit);
        if (!tableConstraints.isEmpty()) {
            sqlCreateTableStatementBuilder = sqlCreateTableStatementBuilder.withTableConstraints(tableConstraints);
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = sqlCreateTableStatementBuilder.build();
        Database.instance.get().execute(createTableStatement, unit);
        Database.instance.get().commit();
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Drops the table of the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void dropTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull SQLQualifiedTable tableName = SQLExplicitlyQualifiedTableBuilder.withTable(SQLTableNameBuilder.withString(converter.getTypeName()).build()).withSchema(SQLSchemaNameBuilder.withString(unit.getName()).build()).build();
        final @Nonnull SQLDropTableStatement dropTableStatement = SQLDropTableStatementBuilder.withTable(tableName).build();
        Database.instance.get().execute(dropTableStatement, unit);
        Database.instance.get().commit();
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insert(@Nonnull Converter<TYPE, ?> converter, @Nonnull TYPE object, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnNames(converter, columns, "");
        final @Nonnull ImmutableList<@Nonnull SQLParameter> row = ImmutableList.withElementsOf(InfiniteIterable.repeat(SQLParameter.INSTANCE).limit(columns.size()));
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(SQLExpressionsBuilder.withExpressions(row).build())).build();
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(converter.getTypeName()).build();
        final @Nonnull SQLSchemaName schema = SQLSchemaNameBuilder.withString(unit.getName()).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
        final SQLInsertStatement sqlInsertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(ImmutableList.withElementsOf(columns)).withValues(rows).build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(sqlInsertStatement, unit);
        actionEncoder.encodeObject(converter, object);
        actionEncoder.execute();
    }
    
    /**
     * Inserts or updates the given object with the given converter into its table in the given unit.
     */
    @TODO(task="Do we need to allow keys/where clauses that indicate which row should be updated if already inserted?", date="2017-02-05", author = Author.STEPHANIE_STROKA)
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrUpdate(@Nonnull Converter<TYPE, ?> converter, @Nonnull TYPE object, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnNames(converter, columns, "");
    
        final @Nonnull ImmutableList<@Nonnull SQLParameter> row = ImmutableList.withElementsOf(InfiniteIterable.repeat(SQLParameter.INSTANCE).limit(columns.size()));
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(SQLExpressionsBuilder.withExpressions(row).build())).build();
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(converter.getTypeName()).build();
        final @Nonnull SQLSchemaName schema = SQLSchemaNameBuilder.withString(unit.getName()).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
        final @Nonnull SQLInsertStatement insertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(ImmutableList.withElementsOf(columns)).withValues(rows).withReplacing(true).build();
        
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(insertStatement, unit);
        actionEncoder.encodeObject(converter, object);
        actionEncoder.execute();
    }
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Updates the columns of the given converter to the values of the given object with the given where condition in the given unit.
     * The where-prefix indicates on which field the where clause should match.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable UPDATE_TYPE, @Unspecifiable WHERE_TYPE> void update(@Nonnull Converter<UPDATE_TYPE, ?> updateConverter, @Nonnull UPDATE_TYPE updateObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nullable WHERE_TYPE whereObject, @Nonnull String wherePrefix, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnNames(updateConverter, columns, "");
    
        final @Nonnull FiniteIterable<SQLAssignment> assignments = columns.map(column -> SQLAssignmentBuilder.withColumn(column).withExpression(SQLParameter.INSTANCE).build());
    
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(updateConverter.getTypeName()).build();
        final @Nonnull SQLSchemaName schema = SQLSchemaNameBuilder.withString(unit.getName()).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
        final @Nonnull SQLUpdateStatementBuilder.@Nonnull InnerSQLUpdateStatementBuilder updateStatementBuilder = SQLUpdateStatementBuilder.withTable(qualifiedTable).withAssignments(ImmutableList.withElementsOf(assignments));
        if (whereConverter != null) {
            final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> whereColumns = FreezableArrayList.withNoElements();
            SQLConversionUtility.fillColumnNames(whereConverter, whereColumns, wherePrefix);
    
            final @Nonnull FiniteIterable<@Nonnull SQLBooleanExpression> whereClauseExpressions = whereColumns.map(column -> SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.EQUAL).withLeftExpression(column).withRightExpression(SQLParameter.INSTANCE).build());
            final @Nonnull SQLBooleanExpression whereClauseExpression = whereClauseExpressions.reduce((left, right) -> SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(left).withRightExpression(right).build());
            updateStatementBuilder.withWhereClause(whereClauseExpression);
        }
        final SQLUpdateStatement updateStatement = updateStatementBuilder.build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(updateStatement, unit);
        actionEncoder.encodeObject(updateConverter, updateObject);
        if (whereConverter != null) {
            actionEncoder.encodeNullableObject(whereConverter, whereObject);
        }
        actionEncoder.execute();
    }
    
    /**
     * Updates the columns of the given converter to the values of the given object with the given where condition in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable UPDATE_TYPE, @Unspecifiable WHERE_TYPE> void update(@Nonnull Converter<UPDATE_TYPE, ?> updateConverter, @Nonnull UPDATE_TYPE updateObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nullable WHERE_TYPE whereObject, @Nonnull Unit unit) throws DatabaseException {
        update(updateConverter, updateObject, whereConverter, whereObject, "", unit);
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Deletes the entries of the given converter's table with the given where condition in the given unit.
     * The where-prefix indicates on which field the where clause should match.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable WHERE_TYPE> void delete(@Nonnull Converter<?, ?> deleteConverter, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nullable WHERE_TYPE whereObject, @Nonnull String wherePrefix, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnNames(deleteConverter, columns, "");
    
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(deleteConverter.getTypeName()).build();
        final @Nonnull SQLSchemaName schema = SQLSchemaNameBuilder.withString(unit.getName()).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
        final @Nonnull SQLDeleteStatementBuilder.@Nonnull InnerSQLDeleteStatementBuilder deleteStatementBuilder = SQLDeleteStatementBuilder.withTable(qualifiedTable);
        
        if (whereConverter != null) {
            final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> whereColumns = FreezableArrayList.withNoElements();
            SQLConversionUtility.fillColumnNames(whereConverter, whereColumns, wherePrefix);
    
            final @Nonnull FiniteIterable<@Nonnull SQLBooleanExpression> whereClauseExpressions = whereColumns.map(column -> SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.EQUAL).withLeftExpression(column).withRightExpression(SQLParameter.INSTANCE).build());
            final @Nonnull SQLBooleanExpression whereClauseExpression = whereClauseExpressions.reduce((left, right) -> SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(left).withRightExpression(right).build());
            deleteStatementBuilder.withWhereClause(whereClauseExpression);
        }
    
        final SQLDeleteStatement deleteStatement = deleteStatementBuilder.build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(deleteStatement, unit);
        if (whereConverter != null) {
            actionEncoder.encodeNullableObject(whereConverter, whereObject);
        }
        actionEncoder.execute();
    }
    
    /**
     * Deletes the entries of the given converter's table with the given where condition in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable WHERE_TYPE> void delete(@Nonnull Converter<?, ?> deleteConverter, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nullable WHERE_TYPE whereObject, @Nonnull Unit unit) throws DatabaseException {
        delete(deleteConverter, whereConverter, whereObject, "", unit);
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Returns the entries of the given converter's table with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the select and where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static @Capturable <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED, @Unspecifiable WHERE_TYPE> @Nonnull @NonNullableElements @NonFrozen FreezableList<SELECT_TYPE> selectAll(@Nonnull Converter<SELECT_TYPE, PROVIDED> selectConverter, @Shared PROVIDED provided, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException, RecoveryException {
        // TODO
        return FreezableArrayList.withNoElements();
    }
    
    /**
     * Returns the first entry of the given converter's table with the given where condition in the given unit or null if there is no such entry.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the select and where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED, @Unspecifiable WHERE_TYPE> @Nullable SELECT_TYPE selectFirst(@Nonnull Converter<SELECT_TYPE, PROVIDED> selectConverter, @Shared PROVIDED provided, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException, RecoveryException {
        final @Nonnull FreezableList<SELECT_TYPE> results = selectAll(selectConverter, provided, whereObject, whereConverter, unit);
        if (results.isEmpty()) { return null; } else { return results.getFirst(); }
    }
    
//    @Pure
//    @NonCommitting
//    private static <T> @Nonnull SQLDecoder getSelectionResult(@Nonnull Converter<T, ?> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site) throws DatabaseException {
//        final @Nonnull SQLOrderedStatements<@Nonnull SQLSelectStatement, @Nonnull SQLSelectFromTableColumnDeclarations> orderedSelectStatements = SQLOrderedStatementCache.INSTANCE.getOrderedSelectStatements(converter);
//        final @Nonnull @NonEmpty ReadOnlyList<@Nonnull SQLSelectStatement> statementsOrderedByExecution = orderedSelectStatements.getStatementsOrderedByExecution();
//        final @Nonnull SQLSelectStatement selectStatement = statementsOrderedByExecution.getFirst();
//        
//        if (statementsOrderedByExecution.size() > 1) {
//            throw new UnsupportedOperationException("Querying referenced tables is not yet supported.");
//        }
//        
//        if (whereClauseExpression != null) {
//            final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
//            selectStatement.setWhereClause(whereClause);
//        }
//    
//        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
//        Log.debugging(selectIntoTableStatementString);
//        final @Nonnull SQLDecoder decoder = DatabaseUtility.getInstance().executeSelect(selectIntoTableStatementString);
//        return decoder;
//    }
//    
//    /**
//     * Builds and executes an SQL select statement based on the given converter, site and where clause expression. Returns exactly one recovered object.
//     */
//    @Pure
//    @NonCommitting
//    public static <T, E> @Nullable T select(@Nonnull Converter<T, E> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
//        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, whereClauseExpression, site);
//        
//        if (!decoder.moveToNextRow()) {
//            return null;
//        }
//        final @Nullable T recoveredObject = converter.recover(decoder, externallyProvided);
//        Require.that(!decoder.moveToNextRow()).orThrow("Not all of the rows have been processed.");
//        
//        return recoveredObject;
//    }
//    
//    /**
//     * Builds and executes an SQL select statement based on the given converter and site. Returns a list of recovered objects.
//     */
//    @Pure
//    @NonCommitting
//    public static <T, E> Set<T> export(@Nonnull Converter<T, E> converter, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
//        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, null, site);
//        
//        Set<T> recoveredObjects = FreezableLinkedHashSetBuilder.build();
//        while (decoder.moveToNextRow()) {
//            decoder.moveToFirstColumn();
//            final @Nonnull T recoveredObject = converter.recover(decoder, externallyProvided);
//            recoveredObjects.add(recoveredObject);
//        }
//        
//        return recoveredObjects;
//    }
    
}
