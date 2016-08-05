package net.digitalid.database.conversion.columndeclarations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.math.NonNegative;

import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLTypeNode;

/**
 * Implements column declarations for the insertion of SQL data. The column declaration is essentially the {@link SQLColumnName SQL column name} type.
 * 
 * @see SQLColumnDeclarations
 */
public class SQLInsertIntoTableColumnDeclarations extends SQLColumnDeclarations<@Nonnull SQLInsertIntoTableColumnDeclarations, @Nonnull SQLColumnName<?>, @Nonnull SQLInsertStatement> {
    
    /* -------------------------------------------------- SQL Column Declarations -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull Pair<@Nonnull SQLColumnName<?>, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> getColumnDeclaration(@Nonnull String columnName, @Nullable SQLTypeNode type, @Nonnull ImmutableList<CustomAnnotation> annotations) {
        return Pair.of(SQLColumnName.get(columnName), annotations);
    }
    
    @Pure
    @Override
    protected @Nonnull Pair<@Nonnull SQLColumnName<?>, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> fromField(@Nonnull CustomField field) {
        return getColumnDeclaration(field.getName(), null, field.getAnnotations());
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLInsertIntoTableColumnDeclarations(@Nonnull String tableName, @NonNegative int currentColumn) {
        super(tableName, currentColumn);
    }
    
    @Pure
    public static @Nonnull SQLInsertIntoTableColumnDeclarations get(@Nonnull String tableName) {
        return new SQLInsertIntoTableColumnDeclarations(tableName, 0);
    }
    
    @Pure
    @Override
    protected @Nonnull SQLInsertIntoTableColumnDeclarations getInstance(@Nonnull String tableName, @NonNegative int currentColumn) {
        return new SQLInsertIntoTableColumnDeclarations(tableName, currentColumn);
    }
    
    /* -------------------------------------------------- Helper Methods -------------------------------------------------- */
    
    @Pure
    @Override
    protected boolean isColumnDeclaration(@Nonnull String columnName, @Nonnull SQLColumnName<?> columnDeclaration) {
        return columnDeclaration.getValue().equals(columnName);
    }
    
    @Pure
    @Override
    protected @Nullable SQLTypeNode getColumnType(@Nonnull SQLColumnName<?> columnDeclaration) {
        return null;
    }
    
    @Pure
    @Override
    protected @Nonnull String getColumnName(@Nonnull SQLColumnName<?> columnDeclaration) {
        return columnDeclaration.getValue();
    }
    
    /* -------------------------------------------------- Statement -------------------------------------------------- */
    
    @Pure
    @Override
    @SuppressWarnings("unchecked")
    public @Nonnull SQLInsertStatement getStatement() {
        return SQLInsertStatement.get(SQLQualifiedTableName.get(tableName), FreezableArrayList.withElementsOf(getColumnDeclarationList().map(Pair::get0)));
    }
    
}
