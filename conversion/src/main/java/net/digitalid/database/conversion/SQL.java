package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.exceptions.FailedValueRecoveryException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedFailureException;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.conversion.columndeclarations.SQLCreateTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLInsertIntoTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLOrderedStatements;
import net.digitalid.database.conversion.columndeclarations.SQLSelectFromTableColumnDeclarations;
import net.digitalid.database.core.Database;
import net.digitalid.database.storage.Site;
import net.digitalid.database.core.Tables;
import net.digitalid.database.core.interfaces.SQLSelectionResult;
import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedCommitException;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;

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
    public static @Nonnull TableImplementation create(@Nonnull String tableName, @Nonnull Site site, @Nonnull Converter<?, ?> converter) throws InternalException, FailedNonCommittingOperationException, FailedCommitException {
        final @Nonnull SQLCreateTableColumnDeclarations columnDeclarations = SQLCreateTableColumnDeclarations.get(tableName, site);
        converter.declare(columnDeclarations);
        
        final @Nonnull SQLOrderedStatements<SQLCreateTableStatement, ? extends SQLCreateTableColumnDeclarations> orderedCreateStatements = columnDeclarations.getOrderedStatements();
    
        @Nullable TableImplementation mainTable = null;
        final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull TableImplementation> constructedTables = FreezableHashMapBuilder.build();
        for (@Nonnull SQLCreateTableStatement sqlCreateTableStatement : orderedCreateStatements.getStatementsOrderedByExecution()) {
            final @Nonnull String createTableStatementString = SQLDialect.getDialect().transcribe(site, sqlCreateTableStatement);
            sqlCreateTableStatement.columnDeclarations.size();
            
            final @Nonnull TableImplementation table = TableImplementation.get(sqlCreateTableStatement, constructedTables, columnDeclarations.getNumberOfColumnsForField());
            constructedTables.put(table.getName(site), table);
            if (table.getName(site).equals(site.getDatabaseName() + "." + tableName)) {
                mainTable = table;
            }
            Tables.add(table, site);
            Log.debugging("Create Statement: " + createTableStatementString);
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
    public static <T> void insert(@Nullable T object, @Nonnull Converter<T, ?> converter, @Nonnull TableImplementation table) throws ExternalException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = table.getName();
        final @Nonnull Site site = qualifiedTableName.site;
        
        final @Nonnull SQLInsertIntoTableColumnDeclarations insertDeclaration = SQLInsertIntoTableColumnDeclarations.get(qualifiedTableName.tableName, site);
        converter.declare(insertDeclaration);
    
        final @Nonnull SQLOrderedStatements<@Nonnull SQLInsertStatement, @Nonnull SQLInsertIntoTableColumnDeclarations> orderedInsertStatements = insertDeclaration.getOrderedStatements();
    
        final @Nonnull SQLValueCollector valueCollector = Database.getInstance().getValueCollector(orderedInsertStatements.getStatementsOrderedByExecution().map(insertStatement -> 
                Pair.of(insertStatement.toPreparedStatement(SQLDialect.getDialect(), site), Tables.get(insertStatement.qualifiedTableName.getValue()))
               ), orderedInsertStatements.getOrderByColumn(), orderedInsertStatements.getColumnCountForGroup());
        converter.convert(object, valueCollector);
        Database.getInstance().execute(valueCollector);
        Database.getInstance().commit();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Builds and executes an SQL select statement based on the given convertible and site.
     */
    @Pure
    public static <T> T select(@Nonnull Converter<T, ?> converter, @Nonnull SQLBooleanExpression whereClauseExpression, @Nonnull TableImplementation table) throws FailedNonCommittingOperationException, FailedValueRecoveryException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = table.getName();
        final @Nonnull Site site = table.getName().site;
        
        final @Nonnull SQLSelectFromTableColumnDeclarations selectDeclaration = SQLSelectFromTableColumnDeclarations.get(qualifiedTableName.tableName, site);
        converter.declare(selectDeclaration);
        
        final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
        
        final @Nonnull SQLSelectStatement selectStatement = selectDeclaration.getSelectStatement(whereClause);
    
        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
        final @Nonnull SQLSelectionResult selectionResult = Database.getInstance().executeSelect(selectIntoTableStatementString);
        // TODO: allow externally provided object.
        return converter.recover(selectionResult, null);
    }
    
}
