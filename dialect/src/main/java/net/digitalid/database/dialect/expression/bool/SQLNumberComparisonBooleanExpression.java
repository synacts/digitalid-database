package net.digitalid.database.dialect.expression.bool;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;

/**
 * An SQL expression that compares two numbers.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLNumberComparisonBooleanExpression extends SQLBooleanExpression, SQLBinaryExpression<SQLComparisonOperator, SQLNumberExpression> {}
