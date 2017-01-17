package net.digitalid.database.dialect.statement.select.unordered.simple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.statement.select.unordered.SQLUnorderedSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLResultColumnOrAllColumns;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLSource;
import net.digitalid.database.subject.site.Site;

/**
 * A simple select statement without an order or limit clause.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLSimpleSelectStatement extends SQLUnorderedSelectStatement {
    
    /* -------------------------------------------------- Distinct -------------------------------------------------- */
    
    /**
     * Returns whether the selected rows should be distinct.
     */
    @Pure
    @Default("true")
    public boolean isDistinct();
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the result columns, which are queried.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<SQLResultColumnOrAllColumns> getColumns();
    
    /* -------------------------------------------------- Sources -------------------------------------------------- */
    
    /**
     * Returns the sources from which the columns are retrieved.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<SQLSource<?>> getSources();
    
    /* -------------------------------------------------- Where Clause -------------------------------------------------- */
    
    /**
     * Returns an optional where clause.
     */
    @Pure
    public @Nullable SQLBooleanExpression getWhereClause();
    
    /* -------------------------------------------------- Group Clause -------------------------------------------------- */
    
    /**
     * Returns an optional group clause.
     */
    @Pure
    public @Nullable SQLGroupByClause getGroupClause();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("SELECT ");
        if (isDistinct()) { string.append("DISTINCT "); }
        dialect.unparse(getColumns(), site, string);
        string.append(" FROM ");
        dialect.unparse(getSources(), site, string);
        final @Nullable SQLBooleanExpression whereClause = getWhereClause();
        if (whereClause != null) {
            string.append(" WHERE ");
            dialect.unparse(whereClause, site, string);
        }
        final @Nullable SQLGroupByClause groupClause = getGroupClause();
        if (groupClause != null) { dialect.unparse(groupClause, site, string); }
    }
    
}
