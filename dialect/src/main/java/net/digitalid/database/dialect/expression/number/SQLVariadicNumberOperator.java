package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLVariadicOperator;

/**
 * This class enumerates the supported variadic number operators.
 */
@Immutable
public enum SQLVariadicNumberOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the greatest number.
     */
    GREATEST("GREATEST"),
    
    /**
     * This operator returns the first non-null number.
     */
    COALESCE("COALESCE");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLVariadicNumberOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
