package net.digitalid.database.conversion.value.iterable;

import net.digitalid.net.root.Copyable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.Embedd;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.PrimaryKey;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLCollectionsConverter extends SQLConverter<Collection<?>> {
    
   
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nullable @NonNullableElements Annotation[] annotations, boolean embedded) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        if (embedded) {
            if (object == null) {
                final @Nonnull Class<?> genericType = getGenericType(annotations);
                SQL.FORMAT.getSQLConverter(genericType).collectValues(null, genericType, valuesList, new Annotation[0]);
            } else {
                // TODO: assert that object is of type collection
                final @Nullable Collection<?> collection = (Collection<?>) object;
    
                final @Nonnull @NullableElements FreezableArrayList<SQLValues> newList = FreezableArrayList.get();
                collectValues(collection, valuesList, newList, annotations, 0);
                valuesList.clear();
                valuesList.addAll(newList);
            }
        } else {
            // In this case, we do not need to add the collection to the list. Instead it is added in insertInfoDependentTables().
        }
    }
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nullable Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        collectValues(object, type, valuesList, annotations, isAnnotatedWithEmbedd(annotations));
    }
    
    @Override 
    public void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        if (isAnnotatedWithEmbedd(annotations)) {
            final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get("_" + columnName + "_index", tableName);
            qualifiedColumnNames.add(qualifiedColumnName);
            final @Nonnull Class<?> genericType = getColumnType(annotations);
            SQL.FORMAT.getConverter(genericType).putColumnNames(type, columnName, tableName, annotations, qualifiedColumnNames);
        }
    }
    
    private void putColumnDeclarationsOfCollectionField(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull Site site, @Nullable @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException {
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get("_" + columnName + "_index"), SQLType.INTEGER32, FreezableArrayList.<SQLColumnDefinition>get(), FreezableArrayList.<SQLColumnConstraint>get());
        columnDeclarations.add(columnDeclaration);
        final @Nonnull Class<?> genericType = getColumnType(annotations);
        SQL.FORMAT.getConverter(genericType).putColumnDeclarations(type, columnName, columnDeclarations, site, genericType.getAnnotations());
    }
    
    @Override 
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull Site site, @Nullable @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: assert that field is of type collections.
        if (isAnnotatedWithEmbedd(annotations)) {
            putColumnDeclarationsOfCollectionField(type, columnName, columnDeclarations, site, annotations);
        }
    }
    
    // TODO: What if this table depends on another table? do we need to call insertIntoDependentTable() recursively?
    // TODO: Can we create a Convertible dynamically and call SQL.insert()? E.g. a List<?> of element, which is not annotated with "@Embedd" could result in the creation of a Convertible that contains the List (this time annotated with @Embedd) and the columns that are identified as the primary keys of the parent of the list.
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException {
        if (!field.isAnnotationPresent(Embedd.class)) {
            final @Nullable @NonNullableElements @NonFrozen FreezableList<SQLColumnName> columnNames = FreezableArrayList.get();
    
            final @Nonnull @NonNullableElements ReadOnlyList<PrimaryKey> primaryKeys = referencedTable.getPrimaryKeys();
            for (@Nonnull PrimaryKey primaryKey : primaryKeys) {
                SQL.FORMAT.getSQLConverter(primaryKey.getType()).putColumnNames(primaryKey.getType(), "_" + referencedTable.getName().tableName + "_" + primaryKey.getColumnName(), null, null, columnNames);
            }
            final @Nonnull SQLColumnName columnName = SQLColumnName.get("_" + field.getName() + "_index");
            columnNames.add(columnName);
            final @Nonnull Class<?> genericType = getColumnType(field.getAnnotations());
            SQL.FORMAT.getSQLConverter(genericType).putColumnNames(field.getType(), field.getName(), null, genericType.getAnnotations(), columnNames);
            
            final @NonFrozen @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList = FreezableArrayList.get();
    
            for (@Nonnull SQLValues primaryKeyCellsOfRow : primaryKeyTableCells) {
                SQLValues values = SQLValues.get();
                for (@Nonnull SQLExpression<?> primaryKeyCell : primaryKeyCellsOfRow.values) {
                    values.addValue(primaryKeyCell);
                }
                valuesList.add(values);
            }
            try {
                collectValues(object, field.getType(), valuesList, field.getAnnotations(), true);
            } catch (NoSuchFieldException e) {
                StoringException.get(field.getType(), e);
            }
            
            for (@Nonnull @NullableElements SQLValues values : valuesList) {
                final @Nonnull SQLInsertStatement sqlInsertStatement = SQLInsertStatement.get(SQLQualifiedTableName.get(field.getName(), site), columnNames, values);
                final @Nonnull String insertIntoTableStatementString = sqlInsertStatement.toPreparedStatement(SQLDialect.getDialect(), site);
                System.out.println("SQL: " + insertIntoTableStatementString);
                @Nonnull ValueCollector valueCollector = Database.getInstance().getValueCollector(insertIntoTableStatementString);
                sqlInsertStatement.storeValues(valueCollector);
                Database.getInstance().execute(valueCollector);
            }
        }
    }
    
    @Override
    public void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        
    }
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        if (!field.isAnnotationPresent(Embedd.class)) {
            final @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> wrappedElementColumnDeclaration = FreezableArrayList.get();
            
            final @Nonnull @NonNullableElements ReadOnlyList<PrimaryKey> primaryKeys = referencedTable.getPrimaryKeys();
            for (@Nonnull PrimaryKey primaryKey : primaryKeys) {
                SQL.FORMAT.getSQLConverter(primaryKey.getType()).putColumnDeclarations(primaryKey.getType(), "_" + referencedTable.getName().tableName + "_" + primaryKey.getColumnName(), wrappedElementColumnDeclaration, referencedTable.getName().site, new Annotation[0]);
            }
            putColumnDeclarationsOfCollectionField(field.getType(), field.getName(), wrappedElementColumnDeclaration, site, field.getAnnotations());
            final @Nonnull SQLCreateTableStatement createCollectionTableStatement = SQLCreateTableStatement.with(SQLQualifiedTableName.get(field.getName(), site), wrappedElementColumnDeclaration);
            final @Nonnull String createTableStatementString = createCollectionTableStatement.toSQL(SQLDialect.getDialect(), site);
            System.out.println("SQL: " + createTableStatementString);
            Database.getInstance().execute(createTableStatementString);
        }
    }
    
    @Override 
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
    
}
