package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLVariadicOperator;

/**
 * This class enumerates the supported variadic number operators.
 */
@Immutable
public enum SQLVariadicNumberOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the greatest number.
     */
    GREATEST(),
    
    /**
     * This operator returns the first non-null number.
     */
    COALESCE();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLVariadicNumberOperator> transcriber = new Transcriber<SQLVariadicNumberOperator>() {
        
        @Pure
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicNumberOperator operator, @Nonnull Site site) throws InternalException {
            switch (operator) {
                case GREATEST: return "GREATEST";
                case COALESCE: return "COALESCE";
                default: throw UnexpectedValueException.with("operator", operator);
            }
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLVariadicNumberOperator> getTranscriber() {
        return transcriber;
    }
    
}
