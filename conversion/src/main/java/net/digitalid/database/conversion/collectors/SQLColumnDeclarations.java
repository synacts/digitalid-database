package net.digitalid.database.conversion.collectors;

import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.conversion.converter.Declaration;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.freezable.annotations.Frozen;

import net.digitalid.database.conversion.exceptions.ConformityViolationException;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.Embedd;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
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
    
    @Pure
    public @Nonnull Map<@Nonnull String, @Nonnull SQLColumnDeclarations> getDependentTablesColumnDeclarations() {
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
            final CustomType.@Nonnull CustomObjectType customObjectType = (CustomType.@Nonnull CustomObjectType) field.getCustomType();
            if (field.isAnnotatedWith(Embedd.class)) {
                customObjectType.getConverter().declare(this);
            } else if (field.isAnnotatedWith(References.class)) {
                final @Nonnull References references = field.getAnnotation(References.class);
                final @Nonnull SQLColumnName<?> columnName = SQLColumnName.get(references.columnName());
                // TODO: prevent naming conflicts.
                columnDeclarationList.add(SQLColumnDeclaration.of(columnName, references.columnType(), null, null));
                final @Nonnull SQLColumnDeclarations referencedTableColumnDeclarations = SQLColumnDeclarations.get(references.foreignTable());
                customObjectType.getConverter().declare(referencedTableColumnDeclarations);
                referencedTablesColumnDeclarations.put(references.foreignTable(), referencedTableColumnDeclarations);
            } else {
                throw ConformityViolationException.with("Expected @" + Embedd.class.getSimpleName() + " or @" + References.class.getSimpleName() + " annotation on non-primitive field type");
            }
        } else if (field.getCustomType().isCompositeType()) {
            if (field.isAnnotatedWith(Embedd.class)) {
                final @Nonnull SQLColumnDeclaration columnDeclaration = fromField(field);
                columnDeclarationList.add(columnDeclaration);
            } else {
                final @Nonnull SQLColumnDeclarations dependentTableColumnDeclarations = SQLColumnDeclarations.get(tableName + "_" + field.getName());
                dependentTablesColumnDeclarations.put(tableName + "_" + field.getName(), dependentTableColumnDeclarations);
            }
        } else {
            final @Nonnull SQLColumnDeclaration columnDeclaration = fromField(field);
            columnDeclarationList.add(columnDeclaration);
        }
    }
    
    @Pure
    private @Nonnull SQLColumnDeclaration fromField(@Nonnull CustomField field) {
        final @Nonnull SQLColumnName<?> columnName = SQLColumnName.get(field.getName());
        final @Nonnull SQLType type = SQLType.of(field.getCustomType());
        final @Nonnull ReadOnlyList<SQLColumnDefinition> columnDefinitions = SQLColumnDefinition.of(field.getAnnotations());
        final @Nonnull ReadOnlyList<SQLColumnConstraint> columnConstraints = SQLColumnConstraint.of(field.getAnnotations(), field.getName());
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(columnName, type, columnDefinitions, columnConstraints);
        return columnDeclaration;
    }
    
}
