package net.digitalid.database.conversion.converter.iterable;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableHashMap;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.converter.SQLConverter;
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
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

import net.digitalid.net.root.Copyable;

/**
 * This abstract class implements methods that are used to convert iterable data structures into SQL.
 */
public abstract class SQLIterableConverter<T> extends SQLConverter<T> {
    
    /* -------------------------------------------------- Helper Methods -------------------------------------------------- */
    
    /**
     * Returns true if the @Embedd annotation was found, false otherwise.
     */
    protected boolean isAnnotatedWithEmbedd(@Nonnull @NonNullableElements Annotation[] annotations) {
        for (@Nonnull Annotation annotation : annotations) {
            if (annotation instanceof Embedd) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the type declared in the @GenericTypes annotation.
     */
    protected Class<?> getGenericType(@Nullable @NonNullableElements Annotation[] annotations) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof GenericTypes) {
                    return ((GenericTypes) annotation).value()[0];
                }
            }
        }
        // TODO: improve exception
        throw new RuntimeException("Expected a @GenericType annotation for the collection.");
    }
    
    /**
     * Recursively walks through GenericType annotations until it finds a type that is not annotated with @GenericType.
     * Then returns the type.
     */
    protected Class<?> getColumnType(@Nullable @NonNullableElements Annotation[] annotations) {
        @Nonnull Class<?> genericType = getGenericType(annotations);
        while (genericType.isAnnotationPresent(GenericTypes.class)) {
            genericType = getColumnType(genericType.getAnnotations());
        }
        return genericType;
    }
    
    /**
     * Returns n copies of the given list.
     */
    protected <C extends Copyable<C>> List<FreezableArrayList<C>> deepCopy(int n, ReadOnlyList<C> list) {
        List<FreezableArrayList<C>> copies = FreezableArrayList.getWithCapacity(n);
        for (int i = 0; i < n; i++) {
            copies.add(FreezableArrayList.<C>getWithCapacity(list.size()));
        }
        for (C c : list) {
            for (FreezableArrayList<C> copy : copies) {
                copy.add(c.copy());
            }
        }
        return copies;
    }
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public @Nonnull SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
        return SQL.FORMAT.getSQLConverter(getGenericType(annotations)).getSQLType(type, annotations);
    }
    
    /* -------------------------------------------------- Value Collector -------------------------------------------------- */
    
    /**
     * Collects values of a collection by creating n copies of the incoming values list (n = size of the collection), checking whether 
     * the elements of the collection are also iterables, if so, going one level deeper by calling the method recursively and if not, 
     * appending the elements of the collection to each copy of the incoming values list.
     */
    private void collectValues(@Nonnull Collection<?> collection, @Nonnull @NonCapturable @NonNullableElements ReadOnlyList<SQLValues> incomingValuesList, @Nonnull @NonCapturable @NonNullableElements FreezableArrayList<SQLValues> outgoingValuesList, @Nonnull @NonNullableElements Annotation[] annotations, int depth) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        
        final @Nonnull Class<?> genericType = getGenericType(annotations);
        final @Nonnull @NonNullableElements List<FreezableArrayList<SQLValues>> copiesOfValuesList = deepCopy(collection.size(), incomingValuesList);
        int i = 0;
        for (@Nullable Object element : collection) {
            final @Nonnull @NonNullableElements FreezableArrayList<SQLValues> copyOfValuesList = copiesOfValuesList.get(i);
            if (Collection.class.isAssignableFrom(genericType) || genericType.isArray()) {
                if (element != null) {
                    collectValues((Collection<?>) element, incomingValuesList, outgoingValuesList, genericType.getAnnotations(), depth++);
                } else {
                    collectValues(null, genericType, new Annotation[0], copyOfValuesList);
                }
            } else {
                SQL.FORMAT.getSQLConverter(Integer.class).collectValues(depth, Integer.class, new Annotation[0], copyOfValuesList);
                SQL.FORMAT.getSQLConverter(genericType).collectValues(element, genericType, genericType.getAnnotations(), copyOfValuesList);
                outgoingValuesList.addAll(copyOfValuesList);
                i++;
            }
        }
    }
    
    /**
     * Returns the iterable object as a collection, either by casting or by transferring the elements from an array to a collection. 
     */
    protected abstract @Nonnull @NullableElements Collection<?> getCollection(@Nonnull Object object);
    
    /**
     * Collects the values of a collection if the collection is annotated with @Embedd.
     */
    protected void collectValues(@Nullable Object object, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nonnull @NonNullableElements Annotation[] annotations, boolean embedded) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        if (embedded) {
            if (object == null) {
                final @Nonnull Class<?> genericType = getGenericType(annotations);
                SQL.FORMAT.getSQLConverter(genericType).collectValues(null, genericType, new Annotation[0], valuesList);
            } else {
                // TODO: assert that object is of type collection
                final @Nullable Collection<?> collection = getCollection(object);
    
                final @Nonnull @NullableElements FreezableArrayList<SQLValues> newList = FreezableArrayList.get();
                collectValues(collection, valuesList, newList, annotations, 0);
                valuesList.clear();
                valuesList.addAll(newList);
            }
        }
        // else: In this case, we do not need to add the collection to the list. Instead it is added in insertInfoDependentTables().
    }
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull Annotation[] annotations, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        collectValues(object, valuesList, annotations, isAnnotatedWithEmbedd(annotations));
    }
    
    @Override
    public void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        if (isAnnotatedWithEmbedd(annotations)) {
            final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get("_" + columnName + "_index", tableName);
            qualifiedColumnNames.add(qualifiedColumnName);
            final @Nonnull Class<?> genericType = getColumnType(annotations);
            SQL.FORMAT.getSQLConverter(genericType).putColumnNames(type, columnName, tableName, annotations, qualifiedColumnNames);
        }
    }
    
    /**
     * Calls the generic type converter of the iterable to retrieve the column declarations of the collection field.
     */
    private void putColumnDeclarationsOfCollectionField(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nullable @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException {
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get("_" + columnName + "_index"), SQLType.INTEGER32, FreezableArrayList.<SQLColumnDefinition>get(), FreezableArrayList.<SQLColumnConstraint>get());
        columnDeclarations.add(columnDeclaration);
        final @Nonnull Class<?> genericType = getColumnType(annotations);
        SQL.FORMAT.getSQLConverter(genericType).putColumnDeclarations(type, columnName, genericType.getAnnotations(), columnDeclarations);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: assert that field is of type collections.
        if (isAnnotatedWithEmbedd(annotations)) {
            putColumnDeclarationsOfCollectionField(type, columnName, columnDeclarations, annotations);
        }
    }
    
    /* -------------------------------------------------- Required and Dependent Tables -------------------------------------------------- */
    
    @Override
    public void createRequiredTables(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {}
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        if (!isAnnotatedWithEmbedd(annotations)) {
            final @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> wrappedElementColumnDeclaration = FreezableArrayList.get();
            
            final @Nonnull @NonNullableElements ReadOnlyList<PrimaryKey> primaryKeys = referencedTable.getPrimaryKeys();
            for (@Nonnull PrimaryKey primaryKey : primaryKeys) {
                SQL.FORMAT.getSQLConverter(primaryKey.getType()).putColumnDeclarations(primaryKey.getType(), "_" + referencedTable.getName().tableName + "_" + primaryKey.getColumnName(), new Annotation[0], wrappedElementColumnDeclaration);
            }
            putColumnDeclarationsOfCollectionField(type, nameOfDependentTable, wrappedElementColumnDeclaration, annotations);
            final @Nonnull Site site = referencedTable.getName().site;
            final @Nonnull SQLCreateTableStatement createCollectionTableStatement = SQLCreateTableStatement.with(SQLQualifiedTableName.get(nameOfDependentTable, site), wrappedElementColumnDeclaration);
            final @Nonnull String createTableStatementString = createCollectionTableStatement.toSQL(SQLDialect.getDialect(), site);
            System.out.println("SQL: " + createTableStatementString);
            Database.getInstance().execute(createTableStatementString);
        }
    }
    
    // TODO: What if this table depends on another table? do we need to call insertIntoDependentTable() recursively?
    // TODO: Can we create a Convertible dynamically and call SQL.insert()? E.g. a List<?> of element, which is not annotated with "@Embedd" could result in the creation of a Convertible that contains the List (this time annotated with @Embedd) and the columns that are identified as the primary keys of the parent of the list. We could do this using annotation processing.
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws StructureException, StoringException, FailedNonCommittingOperationException {
        if (!isAnnotatedWithEmbedd(annotations)) {
            final @Nullable @NonNullableElements @NonFrozen FreezableList<SQLColumnName<?>> columnNames = FreezableArrayList.get();
    
            final @Nonnull @NonNullableElements ReadOnlyList<PrimaryKey> primaryKeys = referencedTable.getPrimaryKeys();
            for (@Nonnull PrimaryKey primaryKey : primaryKeys) {
                SQL.FORMAT.getSQLConverter(primaryKey.getType()).putColumnNames(primaryKey.getType(), "_" + referencedTable.getName().tableName + "_" + primaryKey.getColumnName(), null, new Annotation[0], columnNames);
            }
            final @Nonnull SQLColumnName<?> columnName = SQLColumnName.get("_" + nameOfDependentTable + "_index");
            columnNames.add(columnName);
            final @Nonnull Class<?> genericType = getColumnType(annotations);
            SQL.FORMAT.getSQLConverter(genericType).putColumnNames(type, nameOfDependentTable, null, genericType.getAnnotations(), columnNames);
            
            final @NonFrozen @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList = FreezableArrayList.get();
    
            for (@Nonnull SQLValues primaryKeyCellsOfRow : primaryKeyTableCells) {
                SQLValues values = SQLValues.get();
                for (@Nonnull SQLExpression<?> primaryKeyCell : primaryKeyCellsOfRow.values) {
                    values.addValue(primaryKeyCell);
                }
                valuesList.add(values);
            }
            try {
                collectValues(object, valuesList, annotations, true);
            } catch (NoSuchFieldException e) {
                throw StoringException.get(type, e);
            }
            
            final @Nonnull Site site = referencedTable.getName().site;
            for (@Nonnull @NullableElements SQLValues values : valuesList) {
                final @Nonnull SQLInsertStatement sqlInsertStatement = SQLInsertStatement.get(SQLQualifiedTableName.get(nameOfDependentTable, site), columnNames, values);
                final @Nonnull String insertIntoTableStatementString = sqlInsertStatement.toPreparedStatement(SQLDialect.getDialect(), site);
                System.out.println("SQL: " + insertIntoTableStatementString);
                @Nonnull ValueCollector valueCollector = Database.getInstance().getValueCollector(insertIntoTableStatementString);
                sqlInsertStatement.storeValues(valueCollector);
                Database.getInstance().execute(valueCollector);
            }
        }
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @SuppressWarnings("unchecked")
    protected @Nullable Object[] recoverNullableObjectArray(@NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
    
        // returns _<columnName>_index: 
        int listStart = result.getColumnIndex();
        int listEnd;
        try {
            result.moveToFirstRow();
        } catch (EntryNotFoundException e) {
            return null;
        }
        final @Nonnull @NullableElements Map<Integer, Object> indexedObjects = FreezableHashMap.get();
        do {
            int index = result.getInteger32();
            // TODO: move to first column. Alternatively, select the cell by columnName.
            final @Nonnull Class<?> genericType = getGenericType(annotations);
            final @Nonnull SQLConverter<?> converter = SQL.FORMAT.getSQLConverter(genericType);
            final @Nullable Object recoveredObject = converter.recoverNullable(genericType, result, new Annotation[0]);
            listEnd = result.getColumnIndex();
            result.moveToColumn(listStart);
            indexedObjects.put(index, recoveredObject);
        } while (result.moveToNextRow());
    
        final @Nonnull @NullableElements Object[] elementValues = new Object[indexedObjects.size()];
        // TODO: we should probably ensure that all indices were stored (which means that a null, returned by the map, does indicate that the column value was null and not that the database was corrupted.
        for (int i = 0; i < indexedObjects.size(); i++) {
            elementValues[i] = indexedObjects.get(i);
        }
        
        result.moveToColumn(listEnd);
        return elementValues;
    }
    
}
