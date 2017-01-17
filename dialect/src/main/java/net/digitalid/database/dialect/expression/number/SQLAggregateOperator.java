package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLUnaryOperator;

/**
 * This class enumerates the supported aggregate operators.
 * 
 * @see SQLAggregateNumberExpression
 */
@Immutable
public enum SQLAggregateOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the average value of all non-null values in the column.
     */
    AVG("AVG"),
    
    /**
     * This operator returns the number of non-null values in the column.
     */
    COUNT("COUNT"),
    
    /**
     * This operator returns the maximum value in the column.
     */
    MAX("MAX"),
    
    /**
     * This operator returns the minimum value in the column.
     */
    MIN("MIN"),
    
    /**
     * This operator returns the sum of all values in the column.
     */
    SUM("SUM");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLAggregateOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
