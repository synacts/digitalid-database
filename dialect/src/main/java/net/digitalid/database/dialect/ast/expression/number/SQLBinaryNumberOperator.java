package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBinaryNumberOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        switch (node) {
            case ADDITION: string.append("+"); break;
            case SUBTRACTION: string.append("-"); break;
            case MULTIPLICATION: string.append("*"); break;
            case DIVISION: string.append("/"); break;
            case INTEGER_DIVISION: string.append("DIV"); break;
            case MODULO: string.append("%"); break;
            default: throw InternalException.get(node.name() + " not implemented.");
        }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLBinaryNumberOperator> getTranscriber() {
        return transcriber;
    }
    
}
