package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLSelectionExistsBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLSelectionExistsBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.dialect.statement.SQLStatement;

/**
 * This SQL node represents an SQL select statement.
 * 
 * @see SQLOrderedSelectStatement
 * @see SQLUnorderedSelectStatement
 */
@Immutable
public interface SQLSelectStatement extends SQLStatement, SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {
    
    /* -------------------------------------------------- Exists -------------------------------------------------- */
    
    /**
     * Returns a boolean expression which checks that result set of this select statement is not empty.
     */
    @Pure
    public default @Nonnull SQLSelectionExistsBooleanExpression exists() {
        return SQLSelectionExistsBooleanExpressionBuilder.withSelection(this).build();
    }
    
}