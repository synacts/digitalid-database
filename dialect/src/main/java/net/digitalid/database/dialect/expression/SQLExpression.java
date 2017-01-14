package net.digitalid.database.dialect.expression;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;

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
public interface SQLExpression extends SQLNode {}
