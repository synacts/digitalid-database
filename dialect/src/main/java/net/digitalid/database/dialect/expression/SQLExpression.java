package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLExpressionInSelectionBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLExpressionInSelectionBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLExpressionIsNotNullBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLExpressionIsNotNullBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLExpressionIsNullBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLExpressionIsNullBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * All SQL expressions implement this interface.
 * 
 * @see SQLBooleanExpression
 * @see SQLNumberExpression
 * @see SQLStringExpression
 * 
 * @see SQLUnaryExpression
 * @see SQLBinaryExpression
 * @see SQLVariadicExpression
 * 
 * @see SQLLiteral
 */
@Immutable
public interface SQLExpression extends SQLNode {
    
    /* -------------------------------------------------- Nullability -------------------------------------------------- */
    
    /**
     * Returns a boolean expression which checks that this expression is null.
     */
    @Pure
    public default @Nonnull SQLExpressionIsNullBooleanExpression isNull() {
        return SQLExpressionIsNullBooleanExpressionBuilder.withExpression(this).build();
    }
    
    /**
     * Returns a boolean expression which checks that this expression is not null.
     */
    @Pure
    public default @Nonnull SQLExpressionIsNotNullBooleanExpression isNotNull() {
        return SQLExpressionIsNotNullBooleanExpressionBuilder.withExpression(this).build();
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    /**
     * Returns a boolean expression which checks that this expression is in the result set of the given select statement.
     */
    @Pure
    public default @Nonnull SQLExpressionInSelectionBooleanExpression in(@Nonnull SQLSelectStatement selectStatement) {
        return SQLExpressionInSelectionBooleanExpressionBuilder.withExpression(this).withSelection(selectStatement).build();
    }
    
}
