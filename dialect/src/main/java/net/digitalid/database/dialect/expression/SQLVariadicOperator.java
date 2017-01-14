package net.digitalid.database.dialect.expression;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.dialect.expression.string.SQLVariadicStringOperator;

/**
 * All variadic SQL operator enumerations implement this interface.
 * 
 * @see SQLVariadicNumberOperator
 * @see SQLVariadicStringOperator
 */
@Immutable
public interface SQLVariadicOperator extends SQLOperator {}
