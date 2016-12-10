package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLUnaryOperator;
import net.digitalid.database.subject.Site;

/**
 * This class enumerates the supported unary boolean operators.
 */
@Immutable
public enum SQLUnaryBooleanOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator inverts the value.
     */
    NOT();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLUnaryBooleanOperator> transcriber = new Transcriber<SQLUnaryBooleanOperator>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryBooleanOperator node, @Nonnull Site site)  throws InternalException {
            switch (node) {
                case NOT: return "NOT";
                default: throw UnexpectedValueException.with("node", node);
            }
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLUnaryBooleanOperator> getTranscriber() {
        return transcriber;
    }
    
}
