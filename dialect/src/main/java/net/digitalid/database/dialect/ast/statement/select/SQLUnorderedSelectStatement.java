package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * Creates an SQL select statement node without ORDER BY clause.
 */
public abstract class SQLUnorderedSelectStatement<T extends SQLUnorderedSelectStatement<T>> implements SQLParameterizableNode<T> {
    
    /**
     * The list of result columns, which are queried.
     */
    public final @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns;
    
    /**
     * The list of sources from which the columns are retrieved.
     */
    public final @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<? extends SQLSource<?>> sources;
    
    /**
     * An optional where clause.
     */
    public final @Nullable SQLWhereClause whereClause;
    
    /**
     * An optional group-by clause.
     */
    public final @Nullable SQLGroupByClause groupByClause;
    
    /**
     * An optional compound operator.
     */
    public final @Nullable SQLCompoundOperator compoundOperator;
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        for (@Nonnull SQLSource<?> source : sources) {
            source.storeValues(collector);
        }
        if (whereClause != null) {
            whereClause.storeValues(collector);
        }
        if (groupByClause != null) {
            groupByClause.storeValues(collector);
        }
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new unordered select statement node with a given list of result columns, a list of sources, and optional where-, and group-by clause and
     * compunt operator.
     */
    protected SQLUnorderedSelectStatement(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<? extends SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLCompoundOperator compoundOperator) {
        this.resultColumns = resultColumns;
        this.sources = sources;
        this.whereClause = whereClause;
        this.groupByClause = groupByClause;
        this.compoundOperator = compoundOperator;
    }
    
}
