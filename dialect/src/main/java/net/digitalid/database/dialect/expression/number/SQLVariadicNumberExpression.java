package net.digitalid.database.dialect.expression.number;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLVariadicExpression;

/**
 * A variadic number expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLVariadicNumberExpression extends SQLNumberExpression, SQLVariadicExpression<SQLVariadicNumberOperator, SQLNumberExpression> {}
