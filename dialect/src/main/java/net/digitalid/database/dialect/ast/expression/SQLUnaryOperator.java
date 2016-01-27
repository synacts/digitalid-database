package net.digitalid.database.dialect.ast.expression;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.expression.bool.SQLUnaryBooleanOperator;
import net.digitalid.database.dialect.ast.expression.number.SQLUnaryNumberOperator;

/**
 * All unary operator enumerations implement this interface.
 * 
 * @see SQLUnaryBooleanOperator
 * @see SQLUnaryNumberOperator
 */
public interface SQLUnaryOperator extends SQLNode {}
