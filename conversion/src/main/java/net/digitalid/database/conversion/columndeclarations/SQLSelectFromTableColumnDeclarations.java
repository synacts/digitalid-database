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

import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.select.SQLQualifiedTableNameSource;
import net.digitalid.database.dialect.ast.statement.select.SQLResultColumn;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLSource;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLTypeNode;

/**
 * Implements column declarations for the retrieval of SQL data. The column declaration is the {@link SQLQualifiedColumnName SQL qualified column name} type.
 *
 * @see SQLColumnDeclarations
 */
public class SQLSelectFromTableColumnDeclarations extends SQLColumnDeclarations<SQLSelectFromTableColumnDeclarations, SQLQualifiedColumnName, SQLSelectStatement> {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLSelectFromTableColumnDeclarations(@Nonnull String tableName, @NonNegative int columnCount) {
        super(tableName, columnCount);
    }
    
    @Pure
    public static @Nonnull SQLSelectFromTableColumnDeclarations get(@Nonnull String tableName) {
        return new SQLSelectFromTableColumnDeclarations(tableName, 0);
    }
    
    @Pure
    @Override
    public @Nonnull SQLSelectFromTableColumnDeclarations getInstance(@Nonnull String tableName, @NonNegative int columnCount) {
        return new SQLSelectFromTableColumnDeclarations(tableName, columnCount);
    }
    
    /* -------------------------------------------------- Column Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull Pair<@Nonnull SQLQualifiedColumnName, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> getColumnDeclaration(@Nonnull String columnName, @Nullable SQLTypeNode type, @Nonnull ImmutableList<@Nonnull CustomAnnotation> annotations) {
        return Pair.of(SQLQualifiedColumnName.get(columnName, tableName), annotations);
    }
    
    @Pure
    @Override
    protected @Nonnull Pair<@Nonnull SQLQualifiedColumnName, @Nonnull ImmutableList<@Nonnull CustomAnnotation>> fromField(@Nonnull CustomField field) {
        return getColumnDeclaration(field.getName(), null, field.getAnnotations());
    }
    
    /* -------------------------------------------------- Helper Methods -------------------------------------------------- */
    
    @Pure
    @Override
    protected boolean isColumnDeclaration(@Nonnull String columnName, @Nonnull SQLQualifiedColumnName columnDeclaration) {
        return columnDeclaration.getValue().equals(tableName + "." + columnName);
    }
    
    @Pure
    @Override
    protected @Nullable SQLTypeNode getColumnType(@Nonnull SQLQualifiedColumnName columnDeclaration) {
        return null;
    }
    
    @Pure
    @Override
    protected @Nonnull String getColumnName(@Nonnull SQLQualifiedColumnName columnDeclaration) {
        return columnDeclaration.getUnqualifiedValue();
    }
    
    /* -------------------------------------------------- Select Statement -------------------------------------------------- */
    
    /**
     * Returns a select statement for a given where clause.
     */
    @Pure
    @SuppressWarnings("unchecked")
    public SQLSelectStatement getSelectStatement(@Nullable SQLWhereClause whereClause) {
        final @Nonnull FreezableArrayList<SQLResultColumn> resultColumns = FreezableArrayList.withInitialCapacity(getColumnDeclarationList().size());
        for (@Nonnull SQLQualifiedColumnName columnName : getColumnDeclarationList().map(Pair::get0)) {
            resultColumns.add(SQLResultColumn.get(columnName, null));
        }
        final @Nonnull SQLSource<?> source = SQLQualifiedTableNameSource.get(SQLQualifiedTableName.get(tableName), null);
        final @Nonnull FreezableArrayList<SQLSource<?>> sources = FreezableArrayList.withInitialCapacity(1);
        sources.add(source);
        
        return SQLSelectStatement.get(resultColumns, sources, whereClause, null, null, null, null);
    }
    
    @Pure
    @Override
    protected SQLSelectStatement getStatement() {
        return getSelectStatement(null);
    }
    
}
