package net.digitalid.database.conversion.columndeclarations;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.Declaration;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.UncheckedException;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.immutable.ImmutableMap;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.math.NonNegative;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.type.Embedded;
import net.digitalid.database.conversion.exceptions.ConformityViolationException;
import net.digitalid.database.dialect.statement.table.create.SQLTypeNode;
import net.digitalid.database.enumerations.SQLType;

/**
 * The SQL column declarations class collects information about the SQL table columns that are required to convert an object to SQL.
 */
public abstract class SQLColumnDeclarations<@Nonnull I extends SQLColumnDeclarations<I, CD, S>, CD, S> implements Declaration {
    
    /* -------------------------------------------------- Column Declaration List -------------------------------------------------- */
    
    /**
     * The list of column declarations.
     */
    protected final @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> columnDeclarationList = FreezableArrayList.withNoElements();
    
    /**
     * Returns the list of column declarations.
     */
    @Pure
    public @Nonnull FiniteIterable<@Nonnull Pair<@Nonnull CD, @Nonnull Integer>> getColumnDeclarationList() {
        return FiniteIterable.of(columnDeclarationList).map(pair -> Pair.of(pair.get0().get0(), pair.get1()));
    }
    
    /* -------------------------------------------------- Current column -------------------------------------------------- */
    
    /**
     * The current column.
     */
    @NonNegative int currentColumn = 0;
    
    /* -------------------------------------------------- Column Declarations of Referenced Tables -------------------------------------------------- */
    
    /**
     * The column declarations for referenced objects.
     */
    private final @Nonnull Map<@Nonnull String, @Nonnull I> referencedTablesColumnDeclarations = FreezableHashMapBuilder.build();
    
    /**
     * Returns the column declarations for referenced objects.
     */
    @Pure
    public @Nonnull Map<@Nonnull String, @Nonnull I> getReferencedTablesColumnDeclarations() {
        return referencedTablesColumnDeclarations;
    }
    
    /* -------------------------------------------------- Column Declarations of Dependent Tables -------------------------------------------------- */
    
    /**
     * The column declarations for objects that are outsourced in other tables.
     */
    private final @Nonnull Map<@Nonnull String, @Nonnull I> dependentTablesColumnDeclarations = FreezableHashMapBuilder.build();
    
    /**
     * Returns the column declarations for objects that are outsourced in other tables.
     */
    @Impure
    public @Nonnull Map<@Nonnull String, @Nonnull I> getDependentTablesColumnDeclarations() {
        return dependentTablesColumnDeclarations;
    }
    
    /* -------------------------------------------------- Number of Columns for Field -------------------------------------------------- */
    
    /**
     * Stores the number of columns for each field.
     */
    private final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull Integer> numberOfColumnsForField = FreezableHashMapBuilder.build();
    
    /**
     * Returns the number of columns for each field.
     */
    @Pure
    public @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull Integer> getNumberOfColumnsForField() {
        return numberOfColumnsForField;
    }
    
    /**
     * The column count per field.
     */
    private final @Nonnull FreezableLinkedList<@Nonnull Integer> columnCountForGroup = FreezableLinkedList.withNoElements();
    
    /**
     * Returns the column count per field.
     */
    @Pure
    public @Nonnull FreezableLinkedList<@Nonnull Integer> getColumnCountForGroup() {
        return columnCountForGroup;
    }
    
    /* -------------------------------------------------- Table Name -------------------------------------------------- */
    
    /**
     * The table name.
     */
    protected final @Nonnull String tableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column declarations object with a given table name. The current column number indicates how many fields have been processed previously.
     */
    protected SQLColumnDeclarations(@Nonnull String tableName, @NonNegative int currentColumn) {
        this.tableName = tableName;
        this.currentColumn = currentColumn;
    }
    
    /* -------------------------------------------------- Abstract Methods -------------------------------------------------- */
    
    /**
     * Returns a pair of the column and the field annotations for a given column name, SQL type and a list of annotations.
     * Additional remark: Not all column declarations have or require the type (e.g. the insert or select declaration). Thus, it is nullable.
     */
    @Pure
    protected abstract @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> getColumnDeclaration(@Nonnull String columnName, @Nullable SQLTypeNode type, @Nonnull ImmutableList<@Nonnull CustomAnnotation> annotations);
    
    /**
     * Returns a pair of the column and the field annotations for a given field.
     */
    @Pure
    protected abstract @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> fromField(@Nonnull CustomField field);
    
    /**
     * Returns a new instance of the column declarations type.
     */
    @Pure
    protected abstract @Nonnull I getInstance(@Nonnull String tableName, @NonNegative int currentColumn);
    
    /**
     * Returns whether the column declaration matches the given column name.
     */
    @Pure
    protected abstract boolean isColumnDeclaration(@Nonnull String columnName, @Nonnull CD columnDeclaration);
    
    /**
     * Returns the column type for a given column declaration if the information is available for the declarations instance.
     */
    @Pure
    protected abstract @Nullable SQLTypeNode getColumnType(@Nonnull CD columnDeclaration);
    
    /**
     * Returns the column name of a given column declaration.
     */
    @Pure
    protected abstract @Nonnull String getColumnName(@Nonnull CD columnDeclaration);
    
    /**
     * Returns the statement constructed from the column declarations.
     */
    @Pure
    protected abstract S getStatement();
    
    /* -------------------------------------------------- Referenced Column Declaration -------------------------------------------------- */
    
    /**
     * Returns the index of the referenced column declaration.
     */
    @Pure
    protected @Nonnull Integer getIndexOfReferencedColumnDeclaration(@Nonnull String columnName, @Nonnull I referencedTable) {
        for (@Nonnull Pair<Pair<CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer> columnOfReferencedTable : referencedTable.columnDeclarationList) {
            if (isColumnDeclaration(columnName, columnOfReferencedTable.get0().get0())) {
                return columnOfReferencedTable.get1();
            }
        }
        throw UncheckedException.with("Could not find column name in referenced table with the name " + Quotes.inSingle(columnName));
    }
    
    /**
     * Adds a referenced column declaration to the column declaration list.
     */
    @Pure
    @SuppressWarnings("unchecked")
    void addReferenceColumnDeclarationToList(@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> columnDeclaration, @Nonnull Integer indexToReferencedColumnDeclaration) {
        columnDeclarationList.add(Pair.of(columnDeclaration, indexToReferencedColumnDeclaration));
        currentColumn++;
    }
    
    /**
     * Add a column declaration to the column declaration list.
     */
    @Pure
    @SuppressWarnings("unchecked")
    private void addColumnDeclarationToList(@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> columnDeclaration) {
        columnDeclarationList.add(Pair.of(columnDeclaration, currentColumn++));
    }
    
    /* -------------------------------------------------- Set Field -------------------------------------------------- */
    
    @Impure
    void declareColumns(Converter<?, ?> converter) {
        final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
        for (@Nonnull CustomField field : fields) {
            setField(field);
        }
    }
    
    @Impure
    @Override
    @SuppressWarnings("unchecked")
    public void setField(@Nonnull CustomField field) {
        int columnCount = currentColumn;
        if (field.getCustomType().isObjectType()) {
            final CustomType.@Nonnull TupleType tupleType = (CustomType.@Nonnull TupleType) field.getCustomType();
            if (field.isAnnotatedWith(Embedded.class)) {
                declareColumns(((CustomType.CustomConverterType) tupleType).getConverter());
            } else if (field.isAnnotatedWith(ForeignKey.class)) {
                final @Nonnull CustomAnnotation references = field.getAnnotation(ForeignKey.class);
                // TODO: prevent naming conflicts.
                final @Nonnull I referencedTableColumnDeclarations = getInstance(references.get("foreignTable", String.class), currentColumn);
                referencedTableColumnDeclarations.declareColumns(((CustomType.CustomConverterType) tupleType).getConverter());
                referencedTablesColumnDeclarations.put(references.get("foreignTable", String.class), referencedTableColumnDeclarations);
                columnCountForGroup.addAll(referencedTableColumnDeclarations.getColumnCountForGroup());
                currentColumn = referencedTableColumnDeclarations.currentColumn;
                final @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> columnDeclaration = getColumnDeclaration(field.getName(), SQLTypeNode.of(references.get("columnType", SQLType.class)), field.getAnnotations());
                addReferenceColumnDeclarationToList(columnDeclaration, getIndexOfReferencedColumnDeclaration(references.get("columnName", String.class), referencedTableColumnDeclarations));
                columnCountForGroup.add(1);
            } else {
                throw ConformityViolationException.with("Expected @$ or @$ annotation on non-primitive field $", Embedded.class.getSimpleName(), ForeignKey.class.getSimpleName(), field.getName());
            }
        } else if (field.getCustomType().isCompositeType()) {
            if (field.isAnnotatedWith(Embedded.class)) {
                @Nonnull CustomType fieldType = field.getCustomType();
                int i = 0;
                while (fieldType.isCompositeType()) {
                    fieldType = ((CustomType.CompositeType) fieldType).getCompositeType();
                    final @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> indexColumnDeclaration = getColumnDeclaration("_" + field.getName() + "_index_" + i, SQLTypeNode.of(SQLType.INTEGER32), ImmutableList.withElementsOfCollection(Collections.emptyList()));
                    addColumnDeclarationToList(indexColumnDeclaration);
                    i++;
                }
                final @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> columnDeclaration = fromField(field);
                addColumnDeclarationToList(columnDeclaration);
                columnCountForGroup.add(currentColumn - columnCount);
            } else {
                final @Nonnull I dependentTableColumnDeclarations = getInstance(tableName + "_" + field.getName(), currentColumn);
                // Remove the @References annotation and add the @Embedd annotation
                final @Nonnull FreezableArrayList<CustomAnnotation> allOtherAnnotations = FreezableArrayList.withInitialCapacity(field.getAnnotations().size() - 1);
                for (@Nonnull CustomAnnotation annotation : field.getAnnotations()) {
                    if (!annotation.getAnnotationType().isAssignableFrom(ForeignKey.class)) {
                        allOtherAnnotations.add(annotation);
                    }
                }
                allOtherAnnotations.add(CustomAnnotation.with(Embedded.class, ImmutableMap.withMappingsOf(Collections.emptyMap())));
                dependentTableColumnDeclarations.setField(CustomField.with(field.getCustomType(), field.getName(), ImmutableList.withElementsOf(allOtherAnnotations)));
                // add columns that reference the primary key(s) of the main table
                dependentTablesColumnDeclarations.put(tableName + "_" + field.getName(), dependentTableColumnDeclarations);
                columnCountForGroup.add(dependentTableColumnDeclarations.currentColumn - currentColumn);
                currentColumn = dependentTableColumnDeclarations.currentColumn;
            }
        } else {
            final @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> columnDeclaration = fromField(field);
            addColumnDeclarationToList(columnDeclaration);
            columnCountForGroup.add(1);
        }
        numberOfColumnsForField.put(field.getName(), currentColumn - columnCount);
    }
    
    /* -------------------------------------------------- Foreign Keys for Dependent Tables -------------------------------------------------- */
    
    /**
     * Returns a list of column declarations with its field annotations and indices that are primary keys.
     */
    @Pure
    protected @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> getPrimaryKeys() {
        @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> primaryKeysWithIndices = FreezableArrayList.withNoElements();
        for (@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer> columnDeclaration : columnDeclarationList) {
            final @Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> column = columnDeclaration.get0();
            for (@Nonnull CustomAnnotation annotation : column.get1()) {
                if (annotation.getAnnotationType().isAssignableFrom(PrimaryKey.class)) {
                    primaryKeysWithIndices.add(columnDeclaration);
                }
            }
        }
        if (primaryKeysWithIndices.isEmpty()) {
            return columnDeclarationList;
        } else {
            return primaryKeysWithIndices;
        }
    }
    
    /**
     * Takes a list of primary keys and converts them to foreign keys, so that outsourced tables can reference the outsourcing table.
     */
    @Pure
    private @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> toForeignKeys(@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> primaryKeys) {
        final @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> foreignKeys = FreezableArrayList.withInitialCapacity(primaryKeys.size());
        for (@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer> primaryKey : primaryKeys) {
            final @Nonnull ImmutableList<@Nonnull CustomAnnotation> primaryKeyAnnotations = primaryKey.get0().get1();
            @Nonnull FreezableArrayList<@Nonnull CustomAnnotation> foreignKeyAnnotations = FreezableArrayList.withInitialCapacity(primaryKeyAnnotations.size());
            for (@Nonnull CustomAnnotation primaryKeyAnnotation : primaryKeyAnnotations) {
                if (!primaryKeyAnnotation.getAnnotationType().isAssignableFrom(PrimaryKey.class)) {
                    foreignKeyAnnotations.add(primaryKeyAnnotation);
                }
            }
            final @Nonnull Map<@Nonnull String, @Nullable Object> annotationFields = FreezableHashMapBuilder.build();
            final @Nonnull CD primaryKeyColumnDeclaration = primaryKey.get0().get0();
            annotationFields.put("columnName", getColumnName(primaryKeyColumnDeclaration));
            annotationFields.put("foreignTable", tableName);
            annotationFields.put("columnType", getColumnType(primaryKeyColumnDeclaration));
            foreignKeyAnnotations.add(CustomAnnotation.with(ForeignKey.class, ImmutableMap.withMappingsOf(annotationFields)));
            @Nonnull final Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> foreignKeyColumnDeclaration = getColumnDeclaration(getColumnName(primaryKeyColumnDeclaration), getColumnType(primaryKeyColumnDeclaration), ImmutableList.withElementsOf(foreignKeyAnnotations));
            foreignKeys.add(Pair.of(foreignKeyColumnDeclaration, primaryKey.get1()));
        }
        return foreignKeys;
    }
    
    /**
     * Creates and adds foreign keys to the outsourced tables.
     */
    @Pure
    protected void addForeignKeysToDependentTables() {
        final @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> primaryKeys = getPrimaryKeys();
        final @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer>> foreignKeys = toForeignKeys(primaryKeys);
        for (@Nonnull I dependentTable : dependentTablesColumnDeclarations.values()) {
            dependentTable.addForeignKeysToDependentTables();
            for (@Nonnull Pair<@Nonnull Pair<@Nonnull CD, @Nonnull ImmutableList<@Nonnull CustomAnnotation>>, @Nonnull Integer> foreignKey : foreignKeys) {
                dependentTable.addReferenceColumnDeclarationToList(foreignKey.get0(), foreignKey.get1());
                columnCountForGroup.add(1);
            }
        }
    }
    
    /* -------------------------------------------------- Ordered Insert Statements -------------------------------------------------- */
    
    /**
     * Returns ordered statements that provide the order for the insertion of the column data and execution of the statements.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public @Nonnull SQLOrderedStatements<S, I> getOrderedStatements() {
        // we need to add all primary keys of the main table to the dependent tables recursively before fetching the ordered statements.
        addForeignKeysToDependentTables();
        final @Nonnull SQLOrderedStatements<S, I> orderedInsertStatements = new SQLOrderedStatements(this);
        return orderedInsertStatements;
    }
    
}
