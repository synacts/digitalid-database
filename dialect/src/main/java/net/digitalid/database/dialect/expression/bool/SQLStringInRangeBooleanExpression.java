package net.digitalid.database.dialect.expression.bool;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.string.SQLStringExpression;

/**
 * An SQL expression that checks whether a string is in a given range.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLStringInRangeBooleanExpression extends SQLExpressionInRangeBooleanExpression<SQLStringExpression> {}
