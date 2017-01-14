package net.digitalid.database.dialect.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLVariadicOperator;

/**
 * This class enumerates the supported variadic string nodes.
 */
@Immutable
public enum SQLVariadicStringOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This node concatenates the strings.
     */
    CONCAT("CONCAT"),
    
    /**
     * This node returns the greatest string.
     */
    GREATEST("GREATEST"),
    
    /**
     * This node returns the first non-null string.
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
    
    private SQLVariadicStringOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
