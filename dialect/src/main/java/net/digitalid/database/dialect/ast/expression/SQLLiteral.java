package net.digitalid.database.dialect.ast.expression;

import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.string.SQLStringLiteral;

/**
 * All literal values implement this interface.
 * 
 * @see SQLBooleanLiteral
 * @see SQLNumberLiteral
 * @see SQLStringLiteral
 */
public interface SQLLiteral extends SQLExpression {}
