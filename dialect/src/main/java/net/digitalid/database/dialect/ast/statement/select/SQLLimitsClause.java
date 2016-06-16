package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.utility.SQLNodeConverter;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * This SQL node represents the SQL limit clause of an SQL select statement.
 */
public class SQLLimitsClause implements SQLParameterizableNode<SQLLimitsClause> {
    
    /**
     * The list of limit clauses.
     */
    public final @Nonnull @NonNullableElements ReadOnlyList<SQLLimitClause> limits;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs an SQL limits clause node with a given list of limit clause nodes.
     */
    private SQLLimitsClause(@Nonnull @NonNullableElements ReadOnlyList<SQLLimitClause> limits) {
        this.limits = limits;
    }
    
    /**
     * Returns an SQL limits clause node with a given list of limit clause nodes.
     */
    public static @Nonnull SQLLimitsClause get(@Nonnull @NonNullableElements ReadOnlyList<SQLLimitClause> limits) {
        return new SQLLimitsClause(limits);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLLimitsClause> transcriber = new Transcriber<SQLLimitsClause>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLLimitsClause node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            string.append("LIMIT ");
            string.append(IterableConverter.toString(node.limits, SQLNodeConverter.get(dialect, site)));
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLLimitsClause> getTranscriber() {
        return transcriber;
    }
    
    @Override 
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        for (@Nonnull SQLLimitClause limitClause : limits) {
            limitClause.storeValues(collector);
        }
    }
    
}
