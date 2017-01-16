package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.size.MinSize;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;

/**
 * Description.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLSimpleSelect extends SQLUnorderedSelect {
    
    /* -------------------------------------------------- Distinct -------------------------------------------------- */
    
    /**
     * Returns whether the selected rows should be distinct.
     */
    @Pure
    @Default("true")
    public boolean isDistinct();
    
    /* -------------------------------------------------- Result Columns -------------------------------------------------- */
    
    /**
     * The list of result columns, which are queried.
     */
    @Pure
    public @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> getResultColumns();
    
    /**
     * The list of sources from which the columns are retrieved.
     */
    @Pure
    public @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<? extends SQLSource<?>> getSources();
    
    /**
     * Returns an optional where clause.
     */
    @Pure
    public @Nullable SQLBooleanExpression getWhereClause();
    
    /**
     * An optional group-by clause.
     */
    @Pure
    public @Nullable SQLGroupByClause getGroupByClause();
    
}
