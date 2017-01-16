package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLOperator;

/**
 * This SQL node represents an SQL compound operator used in an SQL select statement.
 */
@Immutable
public enum SQLCompoundOperator implements SQLOperator {
    
    /**
     * Combines the results of two select statements.
     */
    UNION("UNION"),
    
    /**
     * Intersects the results of two select statements.
     */
    INTERSECT("INTERSECT"),
    
    /**
     * Excludes the results of the second from the first select statement.
     */
    EXCEPT("EXCEPT");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLCompoundOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
