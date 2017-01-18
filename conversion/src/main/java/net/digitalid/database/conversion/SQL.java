package net.digitalid.database.conversion;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.collections.set.FreezableLinkedHashSetBuilder;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UncheckedException;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.columndeclarations.SQLCreateTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLInsertIntoTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLOrderedStatements;
import net.digitalid.database.conversion.columndeclarations.SQLSelectFromTableColumnDeclarations;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.interfaces.TableImplementation;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.DatabaseUtility;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.SQLEncoder;
import net.digitalid.database.interfaces.Tables;
import net.digitalid.database.subject.site.Site;

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
    public static @Nonnull TableImplementation create(@Nonnull Converter<?, ?> converter, @Nonnull Site<?> site) throws InternalException, DatabaseException {
        final @Nonnull String tableName = converter.getTypeName();
        final @Nonnull SQLCreateTableColumnDeclarations columnDeclarations = SQLCreateTableColumnDeclarations.get(tableName);
        for (@Nonnull CustomField field : converter.getFields(Representation.INTERNAL)) {
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
            DatabaseUtility.getInstance().execute(createTableStatementString);
        }
        if (mainTable == null) {
            DatabaseUtility.rollback();
            throw UncheckedException.with("The conversion failed due to a failure in the ordering of tables.");
        }
        DatabaseUtility.commit();
        return mainTable;
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts a given object with a matching converter into a given table by constructing an SQL insert statement and collecting the values of the object.
     */
    @Pure
    @Committing
    public static <T> void insert(@Nullable T object, @Nonnull Converter<T, ?> converter, @Nonnull Site<?> site) throws DatabaseException {
        final @Nonnull SQLOrderedStatements<@Nonnull SQLInsertStatement, @Nonnull SQLInsertIntoTableColumnDeclarations> orderedInsertStatements = SQLOrderedStatementCache.INSTANCE.getOrderedInsertStatements(converter);
        
        final @Nonnull SQLEncoder encoder = DatabaseUtility.getInstance().getValueCollector(orderedInsertStatements.getStatementsOrderedByExecution().map(insertStatement -> 
                insertStatement.toPreparedStatement(SQLDialect.getDialect(), site)
               ), orderedInsertStatements.getOrderByColumn(), orderedInsertStatements.getColumnCountForGroup());
        converter.convert(object, encoder);
        DatabaseUtility.getInstance().execute(encoder);
        DatabaseUtility.getInstance().commit();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    @Pure
    @NonCommitting
    private static <T> @Nonnull SQLDecoder getSelectionResult(@Nonnull Converter<T, ?> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site) throws DatabaseException {
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
        final @Nonnull SQLDecoder decoder = DatabaseUtility.getInstance().executeSelect(selectIntoTableStatementString);
        return decoder;
    }
    
    /**
     * Builds and executes an SQL select statement based on the given converter, site and where clause expression. Returns exactly one recovered object.
     */
    @Pure
    @NonCommitting
    public static <T, E> @Nullable T select(@Nonnull Converter<T, E> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, whereClauseExpression, site);
        
        if (!decoder.moveToNextRow()) {
            return null;
        }
        final @Nullable T recoveredObject = converter.recover(decoder, externallyProvided);
        Require.that(!decoder.moveToNextRow()).orThrow("Not all of the rows have been processed.");
        
        return recoveredObject;
    }
    
    /**
     * Builds and executes an SQL select statement based on the given converter and site. Returns a list of recovered objects.
     */
    @Pure
    @NonCommitting
    public static <T, E> Set<T> export(@Nonnull Converter<T, E> converter, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, null, site);
        
        Set<T> recoveredObjects = FreezableLinkedHashSetBuilder.build();
        while (decoder.moveToNextRow()) {
            decoder.moveToFirstColumn();
            final @Nonnull T recoveredObject = converter.recover(decoder, externallyProvided);
            recoveredObjects.add(recoveredObject);
        }
        
        return recoveredObjects;
    }
    
}
