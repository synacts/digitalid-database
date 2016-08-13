package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

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
    @Pure
    public static @Nonnull SQLLimitsClause get(@Nonnull @NonNullableElements ReadOnlyList<SQLLimitClause> limits) {
        return new SQLLimitsClause(limits);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLLimitsClause> transcriber = new Transcriber<SQLLimitsClause>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLLimitsClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append("LIMIT ");
            string.append(node.limits.map(limit -> dialect.transcribe(site, limit)).join());
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLLimitsClause> getTranscriber() {
        return transcriber;
    }
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        for (@Nonnull SQLLimitClause limitClause : limits) {
            limitClause.storeValues(collector);
        }
    }
    
}
