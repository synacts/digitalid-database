package net.digitalid.database.core.sql.expression;

import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.sql.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.core.sql.expression.string.SQLVariadicStringOperator;

/**
 * All variadic operator enumerations implement this interface.
 * 
 * @see SQLVariadicNumberOperator
 * @see SQLVariadicStringOperator
 */
public interface SQLVariadicOperator extends SQLNode {}
