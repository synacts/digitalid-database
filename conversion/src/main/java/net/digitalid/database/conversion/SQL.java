package net.digitalid.database.conversion;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.exceptions.FailedValueRecoveryException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.logging.Level;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.logging.logger.Logger;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.conversion.collectors.SQLColumnDeclarations;
import net.digitalid.database.conversion.collectors.SQLInsertDeclaration;
import net.digitalid.database.conversion.collectors.SQLOrderedInsertStatements;
import net.digitalid.database.conversion.collectors.SQLSelectDeclaration;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.SQLSelectionResult;
import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedCommitException;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;

/**
 */
@Stateless
public final class SQL {
    
     /* -------------------------------------------------- Format -------------------------------------------------- */
    
    /* -------------------------------------------------- Create -------------------------------------------------- */
    
    /**
     * Creates a table with the given table name at the given site which is capable of storing a
     * set of convertible object specified through the columnGroup parameter and references other tables
     * through the given references parameter.
     * An SQL statement is first constructed into an SQL abstract syntax tree. Afterwards, the AST is
     * transcribed into an SQL string using the loaded dialect. Finally, the SQL string is forwarded to
     * the database instance, which executes the statement. Upon successful execution, a table object is 
     * returned. It may be used for other calls to the SQL class as a reference.
     */
    @Pure
    @Committing
    public static @Nonnull Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull Converter<?> converter) throws InternalException, FailedNonCommittingOperationException, FailedCommitException {
        final @Nonnull SQLColumnDeclarations columnDeclarations = SQLColumnDeclarations.get(tableName);
        converter.declare(columnDeclarations);
        
        final @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> referencedTablesColumnDeclarations = columnDeclarations.getReferencedTablesColumnDeclarations();
        for (Map.Entry<@Nonnull String, @Nonnull SQLColumnDeclarations> referencedTables : referencedTablesColumnDeclarations.entrySet()) {
            final @Nonnull SQLColumnDeclarations referencedColumnDeclarations = referencedTables.getValue();
            final @Nonnull SQLCreateTableStatement referencedTableStatement = referencedColumnDeclarations.getCreateTableStatement(site);
            Database.getInstance().execute(SQLDialect.getDialect().transcribe(site, referencedTableStatement));
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = columnDeclarations.getCreateTableStatement(site);
        final @Nonnull String createTableStatementString = SQLDialect.getDialect().transcribe(site, createTableStatement);
        Database.getInstance().execute(createTableStatementString);
        final @Nonnull Table newTable = Table.get(createTableStatement);
    
        final @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> dependentTablesColumnDeclarations = columnDeclarations.getDependentTablesColumnDeclarations();
        for (Map.Entry<@Nonnull String, @Nonnull SQLColumnDeclarations> dependentTables : dependentTablesColumnDeclarations.entrySet()) {
            final @Nonnull SQLColumnDeclarations dependentColumnDeclarations = dependentTables.getValue();
            final @Nonnull SQLCreateTableStatement dependentCreateTableStatement = dependentColumnDeclarations.getCreateTableStatement(site);
            final @Nonnull String statement = SQLDialect.getDialect().transcribe(site, dependentCreateTableStatement);
            // TODO: change to DEBUGGING
            Logger.log(Level.INFORMATION, "Create Statement: " + statement, null);
            Database.getInstance().execute(statement);
        }
        Database.commit();
        
        return newTable;
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts a given object of a given type into a given table by constructing an SQL insert statement and collecting the values of the object..
     */
    @Pure
    public static <T> void insert(@Nullable T object, @Nonnull Converter<T> converter, @Nonnull Table table) throws ExternalException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = table.getName();
        final @Nonnull Site site = qualifiedTableName.site;
        
        final @Nonnull SQLInsertDeclaration insertDeclaration = SQLInsertDeclaration.get(qualifiedTableName.tableName, site);
        converter.declare(insertDeclaration);
    
        final @Nonnull SQLOrderedInsertStatements orderedInsertStatements = insertDeclaration.getOrderedInsertStatements();
    
        final @Nonnull SQLValueCollector valueCollector = Database.getInstance().getValueCollector(orderedInsertStatements.getInsertStatements().map(insertStatement -> insertStatement.toPreparedStatement(SQLDialect.getDialect(), site)), orderedInsertStatements.getOrder());
        converter.convert(object, valueCollector);
        Database.getInstance().execute(valueCollector);
        Database.getInstance().commit();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Builds and executes an SQL select statement based on the given convertible and site.
     */
    @Pure
    public static <T> T select(@Nonnull Converter<T> converter, @Nonnull SQLBooleanExpression whereClauseExpression, @Nonnull Table table) throws FailedNonCommittingOperationException, FailedValueRecoveryException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = table.getName();
        final @Nonnull Site site = table.getName().site;
        
        final @Nonnull SQLSelectDeclaration selectDeclaration = SQLSelectDeclaration.get(qualifiedTableName.tableName, site);
        converter.declare(selectDeclaration);
        
        final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
        
        final @Nonnull SQLSelectStatement selectStatement = selectDeclaration.getSelectStatement(whereClause);
    
        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
        final @Nonnull SQLSelectionResult selectionResult = Database.getInstance().executeSelect(selectIntoTableStatementString);
        return converter.recover(selectionResult);
    }
    
}
