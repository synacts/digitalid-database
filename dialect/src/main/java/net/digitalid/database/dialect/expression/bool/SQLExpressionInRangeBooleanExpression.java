package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLExpression;

/**
 * An SQL expression that checks whether an expression is in a given range.
 * 
 * @see SQLNumberInRangeBooleanExpression
 * @see SQLStringInRangeBooleanExpression
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLExpressionInRangeBooleanExpression<EXPRESSION extends SQLExpression> extends SQLBooleanExpression {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression which has to be in the given range.
     */
    @Pure
    public @Nonnull EXPRESSION getExpression();
    
    /* -------------------------------------------------- Start Value -------------------------------------------------- */
    
    /**
     * Returns the start value of the range.
     */
    @Pure
    public @Nonnull EXPRESSION getStartValue();
    
    /* -------------------------------------------------- Stop Value -------------------------------------------------- */
    
    /**
     * Returns the stop value of the range.
     */
    @Pure
    public @Nonnull EXPRESSION getStopValue();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("(");
        dialect.unparse(getExpression(), unit, string);
        string.append(") BETWEEN (");
        dialect.unparse(getStartValue(), unit, string);
        string.append(") AND (");
        dialect.unparse(getStopValue(), unit, string);
        string.append(")");
    }
    
}
