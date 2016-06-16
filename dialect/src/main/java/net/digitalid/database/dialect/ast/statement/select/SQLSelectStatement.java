package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.castable.exceptions.InvalidClassCastException;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.statement.insert.SQLValuesOrStatement;
import net.digitalid.database.dialect.ast.utility.SQLNodeConverter;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

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
    protected SQLSelectStatement(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLOrderByClause orderByClause, @Nullable SQLLimitsClause limitsClause, @Nullable SQLCompoundOperator compoundOperator) {
        super(resultColumns, sources, whereClause, groupByClause, compoundOperator);
        this.orderByClause = orderByClause;
        this.limitsClause = limitsClause;
    }
    
    public static @Nonnull SQLSelectStatement get(@Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLResultColumn> resultColumns, @Nonnull @NonNullableElements @Frozen @MinSize(1) ReadOnlyList<SQLSource<?>> sources, @Nullable SQLWhereClause whereClause, @Nullable SQLGroupByClause groupByClause, @Nullable SQLOrderByClause orderByClause, @Nullable SQLLimitsClause limitsClause, @Nullable SQLCompoundOperator compoundOperator) {
        return new SQLSelectStatement(resultColumns, sources, whereClause, groupByClause, orderByClause, limitsClause, compoundOperator);
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        super.storeValues(collector);
        if (limitsClause != null) {
            limitsClause.storeValues(collector);
        }
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLSelectStatement> transcriber = new Transcriber<SQLSelectStatement>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLSelectStatement node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            string.append("SELECT ");
            string.append(IterableConverter.toString(node.resultColumns, SQLNodeConverter.get(dialect, site)));
            string.append(" ");
            string.append(IterableConverter.toString(node.sources, SQLNodeConverter.get(dialect, site)));
            if (node.whereClause != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.whereClause, parameterizable);
            }
            if (node.groupByClause != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.groupByClause, parameterizable);
            }
            if (node.compoundOperator != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.compoundOperator, parameterizable);
            }
            if (node.orderByClause != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.orderByClause, parameterizable);
            }
            if (node.limitsClause != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.limitsClause, parameterizable);
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLSelectStatement> getTranscriber() {
        return transcriber;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public @Nonnull <T> T castTo(@Nonnull Class<T> targetClass) throws InvalidClassCastException {
        Require.that(targetClass.isInstance(this)).orThrow("This object can only be casted to SQLSelectStatement");
        return (T) this;
    }
    
    public String toPreparedStatement(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        dialect.transcribe(site, stringBuilder, this, true);
        return stringBuilder.toString();
    }
    
}
