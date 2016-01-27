package net.digitalid.database.dialect.ast.expression;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.dialect.ast.expression.string.SQLVariadicStringOperator;

/**
 * All variadic operator enumerations implement this interface.
 * 
 * @see SQLVariadicNumberOperator
 * @see SQLVariadicStringOperator
 */
public interface SQLVariadicOperator extends SQLNode {}
