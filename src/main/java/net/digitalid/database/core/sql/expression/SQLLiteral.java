package net.digitalid.database.core.sql.expression;

import net.digitalid.database.core.sql.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.core.sql.expression.number.SQLNumberLiteral;
import net.digitalid.database.core.sql.expression.string.SQLStringLiteral;

/**
 * All literal values implement this interface.
 * 
 * @see SQLBooleanLiteral
 * @see SQLNumberLiteral
 * @see SQLStringLiteral
 */
public interface SQLLiteral extends SQLExpression {}
