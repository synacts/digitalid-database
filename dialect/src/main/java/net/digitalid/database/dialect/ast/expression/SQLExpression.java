package net.digitalid.database.dialect.ast.expression;

import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.ast.expression.string.SQLStringExpression;

/**
 * All expressions implement this interface.
 * 
 * @see SQLBooleanExpression
 * @see SQLNumberExpression
 * @see SQLStringExpression
 * @see SQLVariadicExpression
 */
public interface SQLExpression<T extends SQLExpression<T>> extends SQLParameterizableNode<T> {}
