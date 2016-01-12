package net.digitalid.database.core.sql.expression;

import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.sql.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.core.sql.expression.number.SQLBinaryNumberOperator;

/**
 * All binary operator enumerations implement this interface.
 * 
 * @see SQLBinaryBooleanOperator
 * @see SQLBinaryNumberOperator
 */
public interface SQLBinaryOperator extends SQLNode {}
