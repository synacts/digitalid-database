package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberExpression;
import net.digitalid.database.unit.Unit;

/**
 * All binary SQL expressions implement this interface.
 * 
 * @see SQLBinaryBooleanExpression
 * @see SQLBinaryNumberExpression
 */
@Immutable
public interface SQLBinaryExpression<@Unspecifiable OPERATOR extends SQLBinaryOperator, @Unspecifiable EXPRESSION extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this binary expression.
     */
    @Pure
    public @Nonnull OPERATOR getOperator();
    
    /* -------------------------------------------------- Left Expression -------------------------------------------------- */
    
    /**
     * Returns the left child of this binary expression.
     */
    @Pure
    public @Nonnull EXPRESSION getLeftExpression();
    
    /* -------------------------------------------------- Right Expression -------------------------------------------------- */
    
    /**
     * Returns the right child of this binary expression.
     */
    @Pure
    public @Nonnull EXPRESSION getRightExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("(");
        dialect.unparse(getLeftExpression(), unit, string);
        string.append(") ");
        dialect.unparse(getOperator(), unit, string);
        string.append(" (");
        dialect.unparse(getRightExpression(), unit, string);
        string.append(")");
    }
    
}
