package net.digitalid.database.conversion;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.collections.set.FreezableHashSet;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedFailureException;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.conversion.columndeclarations.SQLCreateTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLInsertIntoTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLOrderedStatements;
import net.digitalid.database.conversion.columndeclarations.SQLSelectFromTableColumnDeclarations;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.Site;
import net.digitalid.database.core.Tables;
import net.digitalid.database.core.interfaces.SQLSelectionResult;
import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * This class serves as an entry point for simple conversion of Java objects to SQL.
 */
@Stateless
public final class SQL {
    
    /* -------------------------------------------------- Create -------------------------------------------------- */
    
    /**
     * Creates a table with the given table name at the given site which is capable of storing a
     * convertible object specified through the given converter.
     * An SQL statement is first constructed into an SQL abstract syntax tree. Afterwards, the AST is
     * transcribed into an SQL string using the loaded dialect. Finally, the SQL string is forwarded to
     * the database instance, which executes the statement. Upon successful execution, a table object is 
     * returned. It may be used for other calls to the SQL class as a reference.
     */
    @Pure
    @Committing
    public static @Nonnull TableImplementation create(@Nonnull Converter<?, ?> converter, @Nonnull Site site) throws InternalException, DatabaseException {
        final @Nonnull String tableName = converter.getName();
        final @Nonnull SQLCreateTableColumnDeclarations columnDeclarations = SQLCreateTableColumnDeclarations.get(tableName);
        for (@Nonnull CustomField field : converter.getFields()) {
            columnDeclarations.setField(field);
        }
        
        final @Nonnull SQLOrderedStatements<SQLCreateTableStatement, ? extends SQLCreateTableColumnDeclarations> orderedCreateStatements = columnDeclarations.getOrderedStatements();
    
        @Nullable TableImplementation mainTable = null;
        final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull TableImplementation> constructedTables = FreezableHashMapBuilder.build();
        for (@Nonnull SQLCreateTableStatement sqlCreateTableStatement : orderedCreateStatements.getStatementsOrderedByExecution()) {
            final @Nonnull String createTableStatementString = SQLDialect.getDialect().transcribe(site, sqlCreateTableStatement);
            sqlCreateTableStatement.columnDeclarations.size();
            
            final @Nonnull TableImplementation table = TableImplementation.get(sqlCreateTableStatement, constructedTables, columnDeclarations.getNumberOfColumnsForField(), site);
            constructedTables.put(table.getName(), table);
            if (table.getName().equals(tableName)) {
                mainTable = table;
            }
            Tables.add(table);
            Log.debugging(createTableStatementString);
            Database.getInstance().execute(createTableStatementString);
        }
        if (mainTable == null) {
            Database.rollback();
            throw UnexpectedFailureException.with("The conversion failed due to a failure in the ordering of tables.");
        }
        Database.commit();
        return mainTable;
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts a given object with a matching converter into a given table by constructing an SQL insert statement and collecting the values of the object.
     */
    @Pure
    public static <T> void insert(@Nullable T object, @Nonnull Converter<T, ?> converter, @Nonnull Site site) throws DatabaseException {
        final @Nonnull SQLOrderedStatements<@Nonnull SQLInsertStatement, @Nonnull SQLInsertIntoTableColumnDeclarations> orderedInsertStatements = SQLOrderedStatementCache.INSTANCE.getOrderedInsertStatements(converter);
        
        final @Nonnull SQLValueCollector valueCollector = Database.getInstance().getValueCollector(orderedInsertStatements.getStatementsOrderedByExecution().map(insertStatement -> 
                insertStatement.toPreparedStatement(SQLDialect.getDialect(), site)
               ), orderedInsertStatements.getOrderByColumn(), orderedInsertStatements.getColumnCountForGroup());
        converter.convert(object, valueCollector);
        Database.getInstance().execute(valueCollector);
        Database.getInstance().commit();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    @Pure
    private static <T> @Nonnull SQLSelectionResult getSelectionResult(@Nonnull Converter<T, ?> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site site) throws DatabaseException {
        final @Nonnull SQLOrderedStatements<@Nonnull SQLSelectStatement, @Nonnull SQLSelectFromTableColumnDeclarations> orderedSelectStatements = SQLOrderedStatementCache.INSTANCE.getOrderedSelectStatements(converter);
        final @Nonnull @NonEmpty ReadOnlyList<@Nonnull SQLSelectStatement> statementsOrderedByExecution = orderedSelectStatements.getStatementsOrderedByExecution();
        final @Nonnull SQLSelectStatement selectStatement = statementsOrderedByExecution.getFirst();
        
        if (statementsOrderedByExecution.size() > 1) {
            throw new UnsupportedOperationException("Querying referenced tables is not yet supported.");
        }
        
        if (whereClauseExpression != null) {
            final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
            selectStatement.setWhereClause(whereClause);
        }
    
        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
        Log.debugging(selectIntoTableStatementString);
        final @Nonnull SQLSelectionResult selectionResult = Database.getInstance().executeSelect(selectIntoTableStatementString);
        return selectionResult;
    }
    
    /**
     * Builds and executes an SQL select statement based on the given converter, site and where clause expression. Returns exactly one recovered object.
     */
    @Pure
    public static <T, E> T select(@Nonnull Converter<T, E> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site site) throws DatabaseException {
        final @Nonnull SQLSelectionResult selectionResult = getSelectionResult(converter, whereClauseExpression, site);
        
        if (!selectionResult.moveToNextRow()) {
            return null;
        }
        final @Nonnull T recoveredObject = converter.recover(selectionResult, null);
        Require.that(!selectionResult.moveToNextRow()).orThrow("Not all of the rows have been processed.");
        
        return recoveredObject;
    }
    
    /**
     * Builds and executes an SQL select statement based on the given converter and site. Returns a list of recovered objects.
     */
    @Pure
    public static <T, E> Set<T> export(@Nonnull Converter<T, E> converter, @Nonnull Site site) throws DatabaseException {
        final @Nonnull SQLSelectionResult selectionResult = getSelectionResult(converter, null, site);
        
        Set<T> recoveredObjects = FreezableHashSet.withElements();
        while (selectionResult.moveToNextRow()) {
            selectionResult.moveToFirstColumn();
            final @Nonnull T recoveredObject = converter.recover(selectionResult, null);
            recoveredObjects.add(recoveredObject);
        }
        
        return recoveredObjects;
    }
    
}
