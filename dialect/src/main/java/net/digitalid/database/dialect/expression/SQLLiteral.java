package net.digitalid.database.dialect.expression;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.expression.string.SQLStringLiteral;

/**
 * All literal SQL values implement this interface.
 * 
 * @see SQLBooleanLiteral
 * @see SQLNumberLiteral
 * @see SQLStringLiteral
 */
@Immutable
public interface SQLLiteral extends SQLExpression {}
