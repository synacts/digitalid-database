package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.string.iterable.Brackets;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.string.iterable.NonNullableElementConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.utility.SQLNodeConverter;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

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
    public final @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource<?>> sources;
    
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
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
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
    protected SQLUnorderedSelectStatement(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLCompoundOperator compoundOperator) {
        this.resultColumns = resultColumns;
        this.sources = sources;
        this.whereClause = whereClause;
        this.groupByClause = groupByClause;
        this.compoundOperator = compoundOperator;
    }
    
}
