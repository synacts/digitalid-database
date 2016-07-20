package net.digitalid.database.conversion.collectors;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.conversion.converter.Declaration;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.immutable.ImmutableMap;

import net.digitalid.database.conversion.exceptions.ConformityViolationException;
import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.annotations.Embedd;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLForeignKeyConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLPrimaryKeyConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;

/**
 *
 */
public class SQLColumnDeclarations implements Declaration {
    
    private final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarationList = FreezableArrayList.withNoElements();
    
    @Pure
    public @Nonnull @Frozen ReadOnlyList<@Nonnull SQLColumnDeclaration> getColumnDeclarationList() {
        return columnDeclarationList;
    }
    
    @Pure
    public @Nonnull SQLCreateTableStatement getCreateTableStatement(@Nonnull Site site) {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = SQLQualifiedTableName.get(tableName, site);
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.with(qualifiedTableName, columnDeclarationList);
        return createTableStatement;
    }
    
    private final @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> referencedTablesColumnDeclarations = FreezableHashMap.withDefaultCapacity();
    
    @Pure
    public @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> getReferencedTablesColumnDeclarations() {
        return referencedTablesColumnDeclarations;
    }
    
    private final @Nonnull Map<@Nonnull String, SQLColumnDeclarations> dependentTablesColumnDeclarations = FreezableHashMap.withDefaultCapacity();
    
    private boolean addedForeignKeyColumnsToMainTable = false;
    
    @Impure
    public @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> getDependentTablesColumnDeclarations() {
        if (!addedForeignKeyColumnsToMainTable) {
            for (Map.Entry<@Nonnull String, @Nonnull SQLColumnDeclarations> entry : dependentTablesColumnDeclarations.entrySet()) {
                final @Nonnull FreezableArrayList<SQLColumnDeclaration> foreignKeyColumns = getForeignKeyColumnsToMainTable();
                entry.getValue().columnDeclarationList.addAll(foreignKeyColumns);
            }
            addedForeignKeyColumnsToMainTable = true;
        }
        return dependentTablesColumnDeclarations;
    }
    
    private final @Nonnull String tableName;
    
    private SQLColumnDeclarations(@Nonnull String tableName) {
        this.tableName = tableName;
    }
    
    @Pure
    public static @Nonnull SQLColumnDeclarations get(@Nonnull String tableName) {
        return new SQLColumnDeclarations(tableName);
    }
    
    @Impure
    @Override
    public void setField(@Nonnull CustomField field) {
        if (field.getCustomType().isObjectType()) {
            final CustomType.@Nonnull TupleType tupleType = (CustomType.@Nonnull TupleType) field.getCustomType();
            if (field.isAnnotatedWith(Embedd.class)) {
                ((CustomType.CustomConverterType) tupleType).getConverter().declare(this);
            } else if (field.isAnnotatedWith(References.class)) {
                final @Nonnull CustomAnnotation references = field.getAnnotation(References.class);
                final @Nonnull SQLColumnName<?> columnName = SQLColumnName.get(field.getName());
                // TODO: prevent naming conflicts.
                columnDeclarationList.add(SQLColumnDeclaration.of(columnName, references.get("columnType", SQLType.class), SQLColumnDefinition.of(field.getAnnotations()), SQLColumnConstraint.of(field.getAnnotations(), field.getName())));
                final @Nonnull SQLColumnDeclarations referencedTableColumnDeclarations = SQLColumnDeclarations.get(references.get("foreignTable", String.class));
                ((CustomType.CustomConverterType) tupleType).getConverter().declare(referencedTableColumnDeclarations);
                referencedTablesColumnDeclarations.put(references.get("foreignTable", String.class), referencedTableColumnDeclarations);
            } else {
                throw ConformityViolationException.with("Expected @" + Embedd.class.getSimpleName() + " or @" + References.class.getSimpleName() + " annotation on non-primitive field type");
            }
        } else if (field.getCustomType().isCompositeType()) {
            if (field.isAnnotatedWith(Embedd.class)) {
                final @Nonnull SQLColumnDeclaration columnDeclaration = fromField(field);
                final @Nonnull SQLColumnDeclaration indexColumnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get("_" + field.getName() + "_index"), SQLType.INTEGER32, (ImmutableList<SQLColumnDefinition>) null, (ImmutableList<SQLColumnConstraint>) null);
                columnDeclarationList.add(indexColumnDeclaration);
                columnDeclarationList.add(columnDeclaration);
            } else {
                final @Nonnull SQLColumnDeclarations dependentTableColumnDeclarations = SQLColumnDeclarations.get(tableName + "_" + field.getName());
                // Remove the @References annotation and add the @Embedd annotation
                final @Nonnull FreezableArrayList<CustomAnnotation> allOtherAnnotations = FreezableArrayList.withCapacity(field.getAnnotations().size() - 1);
                for (@Nonnull CustomAnnotation annotation : field.getAnnotations()) {
                    if (!annotation.getAnnotationType().isAssignableFrom(References.class)) {
                        allOtherAnnotations.add(annotation);
                    }
                }
                allOtherAnnotations.add(CustomAnnotation.with(Embedd.class, ImmutableMap.withMappingsOf(Collections.emptyMap())));
                dependentTableColumnDeclarations.setField(CustomField.with(field.getCustomType(), field.getName(), ImmutableList.withElementsOf(FiniteIterable.of(allOtherAnnotations))));
                // add columns that reference the primary key(s) of the main table
                dependentTablesColumnDeclarations.put(tableName + "_" + field.getName(), dependentTableColumnDeclarations);
            }
        } else {
            final @Nonnull SQLColumnDeclaration columnDeclaration = fromField(field);
            columnDeclarationList.add(columnDeclaration);
        }
    }
    
    @Pure
    private boolean hasPrimaryKeyConstraint(@Nonnull SQLColumnDeclaration columnDeclaration) {
        for (@Nonnull SQLColumnConstraint columnConstraint : columnDeclaration.columnConstraints) {
            if (columnConstraint instanceof SQLPrimaryKeyConstraint) {
                return true;
            }
        }
        return false;
    }
    
    @Pure
    private @Nonnull SQLColumnDeclaration getReferenceTo(@Nonnull SQLColumnDeclaration columnDeclaration) {
        final @Nonnull FreezableArrayList<SQLColumnConstraint> columnConstraintsForReferencingColumn = FreezableArrayList.withNoElements();
        for (@Nonnull SQLColumnConstraint columnConstraint : columnDeclaration.columnConstraints) {
            if (!(columnConstraint instanceof SQLPrimaryKeyConstraint)) {
                columnConstraintsForReferencingColumn.add(columnConstraint);
            }
        }
        final @Nonnull FreezableHashMap<@Nonnull String, @Nullable Object> annotationEntries = FreezableHashMap.withDefaultCapacity();
        annotationEntries.put("columnName", columnDeclaration.columnName.getValue());
        annotationEntries.put("foreignTable", tableName);
        annotationEntries.put("columnType", columnDeclaration.type);
        columnConstraintsForReferencingColumn.add(SQLForeignKeyConstraint.with(CustomAnnotation.with(References.class, ImmutableMap.withMappingsOf(annotationEntries))));
        return SQLColumnDeclaration.of(columnDeclaration.columnName, columnDeclaration.type, columnDeclaration.columnDefinitions, ImmutableList.withElementsOf(FiniteIterable.of(columnConstraintsForReferencingColumn)));
    }
    
    @Pure
    private @Nonnull FreezableArrayList<SQLColumnDeclaration> getForeignKeyColumnsToMainTable() {
        final @Nonnull FreezableArrayList<SQLColumnDeclaration> primaryKeyColumns = FreezableArrayList.withNoElements();
        final @Nonnull FreezableArrayList<SQLColumnDeclaration> allColumns = FreezableArrayList.withNoElements();
        for (@Nonnull SQLColumnDeclaration columnDeclaration : columnDeclarationList) {
            if (hasPrimaryKeyConstraint(columnDeclaration)) {
                primaryKeyColumns.add(getReferenceTo(columnDeclaration));
            }
            allColumns.add(getReferenceTo(columnDeclaration));
        }
        if (!primaryKeyColumns.isEmpty()) {
            return primaryKeyColumns;
        } else {
            return allColumns;
        }
    }
    
    @Pure
    private @Nonnull SQLColumnDeclaration fromField(@Nonnull CustomField field) {
        final @Nonnull SQLColumnName<?> columnName = SQLColumnName.get(field.getName());
        final @Nonnull SQLType type;
        if (field.getCustomType().isCompositeType()) {
            type = SQLType.of(((CustomType.CompositeType) field.getCustomType()).getCompositeType());
        } else {
            type = SQLType.of(field.getCustomType());
        }
        final @Nonnull ReadOnlyList<SQLColumnDefinition> columnDefinitions = SQLColumnDefinition.of(field.getAnnotations());
        final @Nonnull ReadOnlyList<SQLColumnConstraint> columnConstraints = SQLColumnConstraint.of(field.getAnnotations(), field.getName());
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(columnName, type, columnDefinitions, columnConstraints);
        return columnDeclaration;
    }
    
}
