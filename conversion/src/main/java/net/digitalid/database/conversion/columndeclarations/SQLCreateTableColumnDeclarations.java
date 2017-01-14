package net.digitalid.database.conversion.columndeclarations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.math.NonNegative;

import net.digitalid.database.dialect.identifier.SQLColumnName;
import net.digitalid.database.dialect.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.create.SQLTypeNode;
import net.digitalid.database.enumerations.SQLType;

/**
 * Implements column declarations for the creation of SQL tables. The column declaration is the {@link SQLColumnDeclaration SQL column declaration} type.
 *
 * @see SQLColumnDeclarations
 */
public class SQLCreateTableColumnDeclarations extends SQLColumnDeclarations<SQLCreateTableColumnDeclarations, SQLColumnDeclaration, SQLCreateTableStatement> {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLCreateTableColumnDeclarations(@Nonnull String tableName, int columnCount) {
        super(tableName, columnCount);
    }
    
    @Pure
    public static @Nonnull SQLCreateTableColumnDeclarations get(@Nonnull String tableName) {
        return new SQLCreateTableColumnDeclarations(tableName, 0);
    }
    
    @Pure
    @Override
    protected @Nonnull SQLCreateTableColumnDeclarations getInstance(@Nonnull String tableName, @NonNegative int columnCount) {
        return new SQLCreateTableColumnDeclarations(tableName, columnCount);
    }
    
    /* -------------------------------------------------- Get Column Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Pair<@Nonnull SQLColumnDeclaration, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> getColumnDeclaration(@Nonnull String columnName, @Nullable SQLTypeNode type, @Nonnull ImmutableList<@Nonnull CustomAnnotation> annotations) {
        Require.that(type != null).orThrow("The create table column declarations require information about the type of the column");
        
        final @Nonnull ReadOnlyList<SQLColumnDefinition> columnDefinitions = SQLColumnDefinition.of(annotations);
        final @Nonnull ReadOnlyList<SQLColumnConstraint> columnConstraints = SQLColumnConstraint.of(annotations, columnName);
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(columnName), type, columnDefinitions, columnConstraints);
        return Pair.of(columnDeclaration, annotations);
    }
    
    @Pure
    @Override
    protected @Nonnull Pair<@Nonnull SQLColumnDeclaration, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> fromField(@Nonnull CustomField field) {
        final @Nonnull SQLTypeNode type;
        @Nonnull CustomType fieldType = field.getCustomType();
        while (fieldType.isCompositeType()) {
            fieldType = ((CustomType.CompositeType) fieldType).getCompositeType();
        }
        type = SQLTypeNode.of(SQLType.of(fieldType));
        return getColumnDeclaration(field.getName(), type, field.getAnnotations());
    }
    
    /* -------------------------------------------------- Helper Methods -------------------------------------------------- */
    
    @Pure
    @Override
    protected boolean isColumnDeclaration(@Nonnull String columnName, @Nonnull SQLColumnDeclaration columnDeclaration) {
        return columnDeclaration.columnName.getValue().equals(columnName);
    }
    
    @Pure
    @Override
    protected @Nonnull SQLTypeNode getColumnType(@Nonnull SQLColumnDeclaration columnDeclaration) {
        return columnDeclaration.typeNode;
    }
    
    @Pure
    @Override
    protected @Nonnull String getColumnName(@Nonnull SQLColumnDeclaration columnDeclaration) {
        return columnDeclaration.columnName.getValue();
    }
    
    /* -------------------------------------------------- SQL Statement -------------------------------------------------- */
    
    @Pure
    @Override
    protected SQLCreateTableStatement getStatement() {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = SQLQualifiedTableName.get(tableName);
        final @Nonnull FiniteIterable<@Nonnull Pair<@Nonnull SQLColumnDeclaration, @Nonnull Integer>> columnDeclarationList = getColumnDeclarationList();
        try {
            final FiniteIterable<@Nonnull SQLColumnDeclaration> columnDeclarationList2 = columnDeclarationList.map(Pair::get0);
            final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.with(qualifiedTableName, FreezableArrayList.withElementsOf(columnDeclarationList2));
            return createTableStatement;
        } catch (Throwable t) {
            // stop
            t.printStackTrace();
            throw t;
        }
    }
    
}
