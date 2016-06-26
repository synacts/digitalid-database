package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This SQL node represents a limit claus node.
 */
public class SQLLimitClause implements SQLParameterizableNode<SQLLimitClause> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The limit of the limit clause.
     */
    public final @Nonnull SQLNumberExpression<?> limit;
    
    /**
     * The offset of the limit clause.
     */
    public final @Nullable SQLNumberExpression<?> offset;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new SQL limit clause node with the given limit and offset.
     */
    private SQLLimitClause(@Nonnull SQLNumberExpression<?> limit, @Nullable SQLNumberExpression<?> offset) {
        this.limit = limit;
        this.offset = offset;
    }
    
    /**
     * Returns an SQL limit clause node with the given limit and offset.
     */
    @Pure
    public static @Nonnull SQLLimitClause get(@Nonnull SQLNumberExpression<?> limit, @Nullable SQLNumberExpression<?> offset) {
        return new SQLLimitClause(limit, offset);
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        limit.storeValues(collector);
        if (offset != null) {
            offset.storeValues(collector);
        }
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLLimitClause> transcriber = new Transcriber<SQLLimitClause>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLLimitClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.limit));
            if (node.offset != null) {
                string.append(" OFFSET");
                string.append(dialect.transcribe(site, node.offset));
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLLimitClause> getTranscriber() {
        return transcriber;
    }
    
}
