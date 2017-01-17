package net.digitalid.database.dialect.expression;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberOperator;

/**
 * All binary SQL operator enumerations implement this interface.
 * 
 * @see SQLBinaryExpression
 * 
 * @see SQLBinaryBooleanOperator
 * @see SQLBinaryNumberOperator
 */
@Immutable
public interface SQLBinaryOperator extends SQLOperator {}
