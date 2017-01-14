package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported binary boolean SQL operators.
 */
@Immutable
public enum SQLBinaryBooleanOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The logical conjunction.
     */
    AND("AND"),
    
    /**
     * The inclusive disjunction.
     */
    OR("OR"),
    
    /**
     * The exclusive disjunction.
     */
    XOR("XOR"),
    
    /**
     * The logical equality.
     */
    EQUAL("="),
    
    /**
     * The logical inequality.
     * The same as {@link #XOR}.
     */
    UNEQUAL("!=");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLBinaryBooleanOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
