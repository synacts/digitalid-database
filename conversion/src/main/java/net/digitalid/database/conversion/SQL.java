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
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.exceptions.DatabaseException;
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
        // TODO
//        final @Nonnull String tableName = converter.getTypeName();
//        final @Nonnull SQLCreateTableColumnDeclarations columnDeclarations = SQLCreateTableColumnDeclarations.get(tableName);
//        for (@Nonnull CustomField field : converter.getFields(Representation.INTERNAL)) {
//            columnDeclarations.setField(field);
//        }
//        
//        final @Nonnull SQLOrderedStatements<SQLCreateTableStatement, ? extends SQLCreateTableColumnDeclarations> orderedCreateStatements = columnDeclarations.getOrderedStatements();
//    
//        @Nullable TableImplementation mainTable = null;
//        final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull TableImplementation> constructedTables = FreezableHashMapBuilder.build();
//        for (@Nonnull SQLCreateTableStatement sqlCreateTableStatement : orderedCreateStatements.getStatementsOrderedByExecution()) {
//            final @Nonnull String createTableStatementString = SQLDialect.getDialect().transcribe(site, sqlCreateTableStatement);
//            sqlCreateTableStatement.columnDeclarations.size();
//            
//            final @Nonnull TableImplementation table = TableImplementation.get(sqlCreateTableStatement, constructedTables, columnDeclarations.getNumberOfColumnsForField(), site);
//            constructedTables.put(table.getName(), table);
//            if (table.getName().equals(tableName)) {
//                mainTable = table;
//            }
//            Tables.add(table);
//            Log.debugging(createTableStatementString);
//            DatabaseUtility.getInstance().execute(createTableStatementString);
//        }
//        if (mainTable == null) {
//            DatabaseUtility.rollback();
//            throw UncheckedException.with("The conversion failed due to a failure in the ordering of tables.");
//        }
//        DatabaseUtility.commit();
//        return mainTable;
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Drops the table of the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void dropTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts the given object with the given converter into its table in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insert(@Nonnull TYPE object, @Nonnull Converter<TYPE, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
//        final @Nonnull SQLOrderedStatements<@Nonnull SQLInsertStatement, @Nonnull SQLInsertIntoTableColumnDeclarations> orderedInsertStatements = SQLOrderedStatementCache.INSTANCE.getOrderedInsertStatements(converter);
//        
//        final @Nonnull SQLEncoder encoder = DatabaseUtility.getInstance().getValueCollector(orderedInsertStatements.getStatementsOrderedByExecution().map(insertStatement -> 
//                insertStatement.toPreparedStatement(SQLDialect.getDialect(), site)
//               ), orderedInsertStatements.getOrderByColumn(), orderedInsertStatements.getColumnCountForGroup());
//        converter.convert(object, encoder);
//        DatabaseUtility.getInstance().execute(encoder);
//        DatabaseUtility.getInstance().commit();
    }
    
    /**
     * Inserts or updates the given object with the given converter into its table in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrUpdate(@Nonnull TYPE object, @Nonnull Converter<TYPE, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Updates the columns of the given converter to the values of the given object with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the update and the where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable UPDATE_TYPE, @Unspecifiable WHERE_TYPE> void update(@Nullable UPDATE_TYPE updateObject, @Nonnull Converter<UPDATE_TYPE, ?> updateConverter, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Deletes the entries of the given converter's table with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for the where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable WHERE_TYPE> void delete(@Nonnull Converter<?, ?> deleteConverter, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Returns the entries of the given converter's table with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the select and where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED, @Unspecifiable WHERE_TYPE> @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableList<SELECT_TYPE> selectAll(@Nonnull Converter<SELECT_TYPE, PROVIDED> selectConverter, @Shared PROVIDED provided, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException, RecoveryException {
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
