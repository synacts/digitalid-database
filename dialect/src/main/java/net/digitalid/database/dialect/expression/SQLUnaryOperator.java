package net.digitalid.database.dialect.expression;

import net.digitalid.database.dialect.expression.bool.SQLUnaryBooleanOperator;
import net.digitalid.database.dialect.expression.number.SQLUnaryNumberOperator;

/**
 * All unary operator enumerations implement this interface.
 * 
 * @see SQLUnaryBooleanOperator
 * @see SQLUnaryNumberOperator
 */
public interface SQLUnaryOperator extends SQLOperator {}
