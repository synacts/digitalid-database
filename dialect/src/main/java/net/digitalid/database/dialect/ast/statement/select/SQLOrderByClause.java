package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This SQL node represents the order clause of an SQL select statement.
 */
public class SQLOrderByClause implements SQLNode<SQLOrderByClause> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * A list of ordering terms.
     */
    public final @Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLOrderingTerm> orderingTerms;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new order-by clause node with a given list of ordering terms.
     */
    private SQLOrderByClause(@Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLOrderingTerm> orderingTerms) {
        this.orderingTerms = orderingTerms;
    }
    
    /**
     * Returns a new order-by clause node with a given list of ordering terms.
     */
    @Pure
    public static @Nonnull SQLOrderByClause get(@Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLOrderingTerm> orderingTerms) {
        return new SQLOrderByClause(orderingTerms);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLOrderByClause> transcriber = new Transcriber<SQLOrderByClause>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLOrderByClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append("ORDER BY ");
            string.append(node.orderingTerms.map(orderingTerm -> dialect.transcribe(site, orderingTerm)).join());
            return string.toString();
        }
        
    };
    
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLOrderByClause> getTranscriber() {
        return transcriber;
    }
    
}
