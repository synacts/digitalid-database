package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberExpression;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

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
    public static @Nonnull SQLLimitClause get(@Nonnull SQLNumberExpression<?> limit, @Nullable SQLNumberExpression<?> offset) {
        return new SQLLimitClause(limit, offset);
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLLimitClause node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.limit, parameterizable);
            if (node.offset != null) {
                string.append(" OFFSET");
                dialect.transcribe(site, string, node.offset, parameterizable);
            }
        }
        
    };
    
    @Override 
    public @Nonnull Transcriber<SQLLimitClause> getTranscriber() {
        return transcriber;
    }
    
}
