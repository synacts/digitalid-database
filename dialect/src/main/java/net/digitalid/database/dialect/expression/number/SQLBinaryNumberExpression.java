package net.digitalid.database.dialect.expression.number;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryExpression;

/**
 * A binary number expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLBinaryNumberExpression extends SQLNumberExpression, SQLBinaryExpression<SQLBinaryNumberOperator, SQLNumberExpression> {}
