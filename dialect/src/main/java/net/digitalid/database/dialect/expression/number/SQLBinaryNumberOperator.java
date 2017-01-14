package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported binary number operators.
 */
@Immutable
public enum SQLBinaryNumberOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator represents addition.
     */
    ADDITION("+"),
    
    /**
     * This operator represents subtraction.
     */
    SUBTRACTION("-"),
    
    /**
     * This operator represents multiplication.
     */
    MULTIPLICATION("*"),
    
    /**
     * This operator represents division.
     */
    DIVISION("/"),
    
    /**
     * This operator represents integer division.
     */
    INTEGER_DIVISION("DIV"),
    
    /**
     * This operator represents modulo.
     */
    MODULO("%");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLBinaryNumberOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
