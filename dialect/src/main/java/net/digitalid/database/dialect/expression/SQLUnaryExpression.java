package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLUnaryBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLUnaryNumberExpression;
import net.digitalid.database.unit.Unit;

/**
 * All unary SQL expressions implement this interface.
 * 
 * @see SQLUnaryBooleanExpression
 * @see SQLUnaryNumberExpression
 */
@Immutable
public interface SQLUnaryExpression<@Unspecifiable OPERATOR extends SQLUnaryOperator, @Unspecifiable EXPRESSION extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this unary expression.
     */
    @Pure
    public @Nonnull OPERATOR getOperator();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the child expression of this unary expression.
     */
    @Pure
    public @Nonnull EXPRESSION getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getOperator(), unit, string);
        string.append("(");
        dialect.unparse(getExpression(), unit, string);
        string.append(")");
    }
    
}
