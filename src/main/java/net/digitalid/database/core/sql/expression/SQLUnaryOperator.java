package net.digitalid.database.core.sql.expression;

import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.sql.expression.bool.SQLUnaryBooleanOperator;
import net.digitalid.database.core.sql.expression.number.SQLUnaryNumberOperator;

/**
 * All unary operator enumerations implement this interface.
 * 
 * @see SQLUnaryBooleanOperator
 * @see SQLUnaryNumberOperator
 */
public interface SQLUnaryOperator extends SQLNode {}
