package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported binary number operators.
 */
@Immutable
public enum SQLBinaryNumberOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator represents addition.
     */
    ADDITION(),
    
    /**
     * This operator represents subtraction.
     */
    SUBTRACTION(),
    
    /**
     * This operator represents multiplication.
     */
    MULTIPLICATION(),
    
    /**
     * This operator represents division.
     */
    DIVISION(),
    
    /**
     * This operator represents integer division.
     */
    INTEGER_DIVISION(),
    
    /**
     * This operator represents modulo.
     */
    MODULO();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLBinaryNumberOperator> transcriber = new Transcriber<SQLBinaryNumberOperator>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBinaryNumberOperator node, @Nonnull Site site)  throws InternalException {
            switch (node) {
                case ADDITION: return "+";
                case SUBTRACTION: return "-";
                case MULTIPLICATION: return "*";
                case DIVISION: return "/";
                case INTEGER_DIVISION: return "DIV";
                case MODULO: return "%";
                default: throw UnexpectedValueException.with("node", node);
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLBinaryNumberOperator> getTranscriber() {
        return transcriber;
    }
    
}
