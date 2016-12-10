package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.statement.insert.SQLValuesOrStatement;
import net.digitalid.database.subject.Site;

/**
 * This SQL node represents an SQL select statement.
 */
public class SQLSelectStatement extends SQLUnorderedSelectStatement<SQLSelectStatement> implements SQLValuesOrStatement<SQLSelectStatement> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The optional order by clause of the select statement.
     */
    public final @Nullable SQLOrderByClause orderByClause;
    
    /**
     * The optional limit clause of the select statement.
     */
    public final @Nullable SQLLimitsClause limitsClause;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a select statement with a given list or result columns, a list of sources, from where the columns will be retrieved, and the optional 
     * where-, group-by-, order-by-, and limit clause nodes and an optional compound operator. 
     */
    protected SQLSelectStatement(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<? extends SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLOrderByClause orderByClause, @Nullable SQLLimitsClause limitsClause, @Nullable SQLCompoundOperator compoundOperator) {
        super(resultColumns, sources, whereClause, groupByClause, compoundOperator);
        this.orderByClause = orderByClause;
        this.limitsClause = limitsClause;
    }
    
    /**
     * Returns an SQL select statement to query the result columns from the given sources with an optionally given where clause, group-by clause, order clause, limits clause and compound operator.
     */
    @Pure
    public static @Nonnull SQLSelectStatement get(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<? extends SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLOrderByClause orderByClause, @Nullable SQLLimitsClause limitsClause, @Nullable SQLCompoundOperator compoundOperator) {
        return new SQLSelectStatement(resultColumns, sources, whereClause, groupByClause, orderByClause, limitsClause, compoundOperator);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLSelectStatement> transcriber = new Transcriber<SQLSelectStatement>() {
        
        @Pure
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLSelectStatement node, @Nonnull Site site) throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append("SELECT ");
            string.append(node.resultColumns.map((resultColumn) -> dialect.transcribe(site, resultColumn)).join(", "));
            string.append(" FROM ");
            string.append(node.sources.map((source) -> dialect.transcribe(site, source)).join(", "));
            if (node.getWhereClause() != null) {
                string.append(" ").append(dialect.transcribe(site, node.getWhereClause()));
            }
            if (node.groupByClause != null) {
                string.append(" ").append(dialect.transcribe(site, node.groupByClause));
            }
            if (node.compoundOperator != null) {
                string.append(" ").append(dialect.transcribe(site, node.compoundOperator));
            }
            if (node.orderByClause != null) {
                string.append(" ").append(dialect.transcribe(site, node.orderByClause));
            }
            if (node.limitsClause != null) {
                string.append(" ").append(dialect.transcribe(site, node.limitsClause));
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLSelectStatement> getTranscriber() {
        return transcriber;
    }
    
    /**
     * Returns a prepared statement as string.
     */
    @Pure
    public @Nonnull String toPreparedStatement(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        return dialect.transcribe(site, this);
    }
    
}
