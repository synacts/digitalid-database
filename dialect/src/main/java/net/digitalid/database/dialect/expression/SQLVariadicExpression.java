package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.number.SQLVariadicNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLVariadicStringExpression;
import net.digitalid.database.subject.site.Site;

/**
 * All variadic expressions implement this interface.
 * 
 * @see SQLVariadicNumberExpression
 * @see SQLVariadicStringExpression
 */
@Immutable
public interface SQLVariadicExpression<@Unspecifiable OPERATOR extends SQLVariadicOperator, @Unspecifiable EXPRESSION extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this variadic expression.
     */
    @Pure
    public @Nonnull OPERATOR getOperator();
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Returns the child expressions of this variadic expression.
     */
    @Pure
    public @Nonnull @NonNullableElements ImmutableList<? extends EXPRESSION> getExpressions();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getOperator(), site, string);
        string.append("(");
        dialect.unparse(getExpressions(), site, string);
        string.append(")");
    }
    
}
