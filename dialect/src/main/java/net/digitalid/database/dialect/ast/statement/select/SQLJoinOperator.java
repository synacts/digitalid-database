package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.subject.site.Site;

/**
 * The SQL node represents a join operator.
 */
public enum SQLJoinOperator implements SQLNode<SQLJoinOperator> {
    
    /* -------------------------------------------------- Join Operator Choices -------------------------------------------------- */
    
    LEFT,
    LEFT_OUTER,
    INNER,
    CROSS,
    NATURAL_LEFT,
    NATURAL_LEFT_OUTER,
    NATURAL_INNER,
    NATURAL_CROSS;
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLJoinOperator> transcriber = new Transcriber<SQLJoinOperator>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLJoinOperator node, @Nonnull Site site)  throws InternalException {
            return node.name().replaceAll("_", " ");
        }
        
    };
 
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLJoinOperator> getTranscriber() {
        return transcriber;
    }
    
}
