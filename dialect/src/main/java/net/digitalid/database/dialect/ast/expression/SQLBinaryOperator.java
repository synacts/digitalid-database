package net.digitalid.database.dialect.ast.expression;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.ast.expression.number.SQLBinaryNumberOperator;

/**
 * All binary operator enumerations implement this interface.
 * 
 * @see SQLBinaryBooleanOperator
 * @see SQLBinaryNumberOperator
 */
public interface SQLBinaryOperator<T> extends SQLNode<T> {}
