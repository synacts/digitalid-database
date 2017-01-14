package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLUnaryOperator;

/**
 * This class enumerates the supported unary number operators.
 */
@Immutable
public enum SQLUnaryNumberOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the rounded value.
     */
    ROUND("ROUND"),
    
    /**
     * This operator returns the negative value.
     */
    NEGATIVE("-"),
    
    /**
     * This operator returns the absolute value.
     */
    ABSOLUTE("ABS");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLUnaryNumberOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
