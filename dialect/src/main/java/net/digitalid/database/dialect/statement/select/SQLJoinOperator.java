package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported join operators.
 */
@Immutable
public enum SQLJoinOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    LEFT("LEFT"),
    LEFT_OUTER("LEFT OUTER"),
    INNER("INNER"),
    CROSS("CROSS"),
    NATURAL_LEFT("NATURAL LEFT"),
    NATURAL_LEFT_OUTER("NATURAL LEFT OUTER"),
    NATURAL_INNER("NATURAL INNER"),
    NATURAL_CROSS("NATURAL CROSS");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLJoinOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
