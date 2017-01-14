package net.digitalid.database.dialect.expression.string;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLVariadicExpression;

/**
 * A variadic string expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLVariadicStringExpression extends SQLStringExpression, SQLVariadicExpression<SQLVariadicStringOperator, SQLStringExpression> {}
