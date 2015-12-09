package net.digitalid.database.core.sql.expression;

import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.expression.bool.SQLBooleanExpression;
import net.digitalid.database.core.sql.expression.number.SQLNumberExpression;
import net.digitalid.database.core.sql.expression.string.SQLStringExpression;

/**
 * All expressions implement this interface.
 * 
 * @see SQLBooleanExpression
 * @see SQLNumberExpression
 * @see SQLStringExpression
 * @see SQLVariadicExpression
 */
public interface SQLExpression extends SQLParameterizableNode {}
