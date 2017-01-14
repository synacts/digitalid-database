package net.digitalid.database.dialect.expression.bool;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.number.SQLNumberExpression;

/**
 * An SQL expression that checks whether a number is in a given range.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLNumberInRangeBooleanExpression extends SQLExpressionInRangeBooleanExpression<SQLNumberExpression> {}
