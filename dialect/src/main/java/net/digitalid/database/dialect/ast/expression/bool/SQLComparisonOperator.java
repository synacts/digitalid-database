package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryOperator;
import net.digitalid.database.subject.Site;

/**
 * This class enumerates the supported comparison operators.
 */
@Immutable
public enum SQLComparisonOperator implements SQLBinaryOperator<SQLComparisonOperator> {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The {@code =} operator.
     */
    EQUAL(),
    
    /**
     * The {@code !=} operator.
     */
    UNEQUAL(),
    
    /**
     * The {@code >=} operator.
     */
    GREATER_OR_EQUAL(),
    
    /**
     * The {@code >} operator.
     */
    GREATER(),
    
    /**
     * The {@code <=} operator.
     */
    LESS_OR_EQUAL(),
    
    /**
     * The {@code <} operator.
     */
    LESS();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLComparisonOperator> transcriber = new Transcriber<SQLComparisonOperator>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLComparisonOperator node, @Nonnull Site site)  throws InternalException {
            switch (node) {
                case EQUAL: return "=";
                case UNEQUAL: return "!=";
                case GREATER_OR_EQUAL: return ">=";
                case GREATER: return ">";
                case LESS_OR_EQUAL: return "<=";
                case LESS: return "<";
                default: throw UnexpectedValueException.with("node", node);
            }
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLComparisonOperator> getTranscriber() {
        return transcriber;
    }
    
}
