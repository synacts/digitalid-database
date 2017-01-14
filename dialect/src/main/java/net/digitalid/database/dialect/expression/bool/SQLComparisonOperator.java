package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported comparison operators.
 */
@Immutable
public enum SQLComparisonOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The {@code =} operator.
     */
    EQUAL("="),
    
    /**
     * The {@code !=} operator.
     */
    UNEQUAL("!="),
    
    /**
     * The {@code >=} operator.
     */
    GREATER_OR_EQUAL(">="),
    
    /**
     * The {@code >} operator.
     */
    GREATER(">"),
    
    /**
     * The {@code <=} operator.
     */
    LESS_OR_EQUAL("<="),
    
    /**
     * The {@code <} operator.
     */
    LESS("<");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLComparisonOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
