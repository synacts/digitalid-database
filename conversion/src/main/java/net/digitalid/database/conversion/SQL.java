package net.digitalid.database.conversion;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.conversion.converter.SQLConverter;
import net.digitalid.database.conversion.exceptions.ConformityViolation;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.select.SQLResultColumn;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLSource;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedCommitException;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 */
@Stateless
public final class SQL {
    
     /* -------------------------------------------------- Format -------------------------------------------------- */
    
    /**
     * The format object of SQL, which contains the SQL converters.
     */
    public static final @Nonnull SQLFormat FORMAT = new SQLFormat();
    
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
    @SafeVarargs
    public static @Nonnull Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull @NonNullableElements Class<? extends Convertible>... columnGroups) throws InternalException, FailedNonCommittingOperationException, StructureException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = SQLQualifiedTableName.get(tableName, site);
        @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations = FreezableArrayList.get();
        
        for (@Nonnull Class<? extends Convertible> columnGroup : columnGroups) {
            @Nonnull SQLConverter<?> sqlConverter = FORMAT.getConverter(columnGroup);
            try {
                // TODO: add table annotations here
                sqlConverter.putColumnDeclarations(columnGroup, tableName, new Annotation[0], columnDeclarations);
            } catch (StructureException | NoSuchFieldException e) {
                throw ConformityViolation.with("Failed to create column declarations for the type '" + columnGroup.getSimpleName() + "' due to conformity problems.", e);
            }
            try {
                sqlConverter.createRequiredTables(columnGroup, columnGroup.getAnnotations(), site);
            } catch (NoSuchFieldException e) {
                throw ConformityViolation.with("Failed to create required tables for the type '" + columnGroup.getSimpleName() + "' due to conformity problems.", e);
            }
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.with(qualifiedTableName, columnDeclarations);
        final @Nonnull String createTableStatementString = createTableStatement.toSQL(SQLDialect.getDialect(), site);
        Database.getInstance().execute(createTableStatementString);
        final @Nonnull Table newTable = Table.get(createTableStatement);
        
        for (@Nonnull Class<? extends Convertible> columnGroup : columnGroups) {
            @Nonnull SQLConverter<?> sqlConverter = FORMAT.getConverter(columnGroup);
            try {
                sqlConverter.createDependentTables(newTable, columnGroup, columnGroup.getSimpleName(), columnGroup.getAnnotations());
            } catch (StructureException | NoSuchFieldException e) {
                throw ConformityViolation.with("Failed to convert the type '" + columnGroup.getSimpleName() + "' due to conformity problems.", e);
            }
        }
        return newTable;
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Returns a list of SQL values containing the given object of the given type converted into SQL.
     */
    private static @Frozen @Nonnull @NonNullableElements ReadOnlyList<SQLValues> getSQLValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) throws InternalException, FailedValueStoringException, StoringException {
        final @NonFrozen @Nonnull @NonNullableElements FreezableArrayList<SQLValues> sqlValues = FreezableArrayList.get();
        final @Nonnull SQLConverter<?> sqlFieldConverter = FORMAT.getConverter(type);
        
        try {
            sqlFieldConverter.collectValues(object, type, annotations, sqlValues);
        } catch (StructureException | NoSuchFieldException e) {
            throw ConformityViolation.with("Failed to convert the type '" + type.getSimpleName() + "' due to conformity problems.", e);
        }
        return sqlValues.freeze();
    }
    
    /**
     * Returns a list of qualified column names collected through the type converter for the given type.
     */
    private static @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLQualifiedColumnName> getQualifiedColumnNames(@Nonnull Class<?> type, @Nullable String tableName) throws StructureException {
        final @Nullable @NonNullableElements @NonFrozen FreezableList<SQLQualifiedColumnName> columnNames = FreezableArrayList.get();
        final @Nonnull SQLConverter<?> sqlFieldConverter = FORMAT.getConverter(type);
        try {
            sqlFieldConverter.putColumnNames(type, type.getSimpleName(), tableName, type.getAnnotations(), columnNames);
        } catch (StructureException e) {
            throw ConformityViolation.with("Failed to convert the field '" + type.getSimpleName() + "' due to conformity problems.", e);
        }
        return columnNames.freeze();
    }
    
    /**
     * Returns a list of unqualified column names by casting the qualified column names.
     */
    @SuppressWarnings("unchecked")
    private static @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLColumnName<?>> getColumnNames(@Nonnull Class<?> type) throws StructureException {
        final @Nullable @NonNullableElements @Frozen ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames = getQualifiedColumnNames(type, null);
        // Since generic types are not covariant, we need to cast here. But we can safely do so, since we know that a qualified column name is always a column name.
        return (ReadOnlyList<SQLColumnName<?>>) (ReadOnlyList<?>) qualifiedColumnNames;
    }
    
    /**
     * Inserts a given object of a given type into a given table by constructing an SQL insert statement and collecting the values of the object..
     */
    public static void insert(@Nullable Object object, @Nonnull Class<?> type, @Nonnull Table table) throws FailedNonCommittingOperationException, InternalException, StoringException, FailedCommitException, StructureException {
        // TODO: what about prefixes?!
        
        final @Nonnull SQLQualifiedTableName qualifiedTableName = table.getName();
        final @Nonnull String tableName = qualifiedTableName.tableName;
        final @Nonnull Site site = qualifiedTableName.site;
        final @Nonnull @NonNullableElements Annotation[] annotations = type.getAnnotations();
        
        // TODO: Explicit transaction definition?
        final @Nonnull @NonNullableElements ReadOnlyList<SQLValues> valuesList = getSQLValues(object, type, annotations);
        for (@Nonnull @NullableElements SQLValues values : valuesList) {
            final @Nonnull SQLInsertStatement sqlInsertStatement = SQLInsertStatement.get(SQLQualifiedTableName.get(tableName, site), getColumnNames(type), values);
            final @Nonnull String insertIntoTableStatementString = sqlInsertStatement.toPreparedStatement(SQLDialect.getDialect(), site);
            
            @Nonnull ValueCollector valueCollector = Database.getInstance().getValueCollector(insertIntoTableStatementString);
            sqlInsertStatement.storeValues(valueCollector);
            Database.getInstance().execute(valueCollector);
        }
        final @Nonnull SQLConverter<?> sqlFieldConverter = FORMAT.getConverter(type);
        sqlFieldConverter.insertIntoDependentTable(table, table.filterPrimaryKeyTableCells(valuesList), object, type, type.getSimpleName(), type.getAnnotations());
        Database.getInstance().commit();
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Builds and executes an SQL select statement based on the given convertible and site.
     */
    public static <T extends Convertible> T select(@Nonnull Class<T> convertible, @Nonnull SQLBooleanExpression whereClauseExpression, @Nonnull Table table) throws FailedNonCommittingOperationException, RecoveryException, StructureException, CorruptNullValueException {
        final @Nonnull Site site = table.getName().site;
        
        final @Nonnull SQLConverter<T> converter = SQL.FORMAT.getSQLConverter(convertible);
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames = getQualifiedColumnNames(convertible, table.getName().tableName);
        final @Nonnull @NonNullableElements FreezableArrayList<SQLResultColumn> resultColumns = FreezableArrayList.get();
        
        int i = 0;
        for (@Nonnull SQLQualifiedColumnName qualifiedColumnName : qualifiedColumnNames) {
            resultColumns.add(SQLResultColumn.get(qualifiedColumnName, "column" + i));
            i++;
        }
        
        final @Nonnull @NonNullableElements FreezableArrayList<SQLSource<?>> sources = FreezableArrayList.get();
        // TODO: if there are references, we need to join them.
        final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
        
        final @Nonnull SQLSelectStatement selectStatement = SQLSelectStatement.get(resultColumns, sources, whereClause, null, null, null, null);
    
        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
        @Nonnull ValueCollector valueCollector = Database.getInstance().getValueCollector(selectIntoTableStatementString);
        selectStatement.storeValues(valueCollector);
        
        @Nonnull SelectionResult selectionResult = Database.getInstance().executeSelect(valueCollector);
        
        return converter.recoverNullable(convertible, selectionResult, new Annotation[0]);
    }
    
}
