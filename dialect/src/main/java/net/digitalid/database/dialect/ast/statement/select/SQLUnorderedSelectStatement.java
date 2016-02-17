package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.dialect.ast.SQLParameterizableNode;

/**
 *
 */
public abstract class SQLUnorderedSelectStatement<T extends SQLUnorderedSelectStatement<T>> implements SQLParameterizableNode<T> {
    
    public final @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns;
    
    public final @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource> sources;
    
    public final @Nullable SQLWhereClause whereClause;
    
    public final @Nullable SQLGroupByClause groupByClause;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected SQLUnorderedSelectStatement(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause) {
        this.resultColumns = resultColumns;
        this.sources = sources;
        this.whereClause = whereClause;
        this.groupByClause = groupByClause;
    }
    
}
