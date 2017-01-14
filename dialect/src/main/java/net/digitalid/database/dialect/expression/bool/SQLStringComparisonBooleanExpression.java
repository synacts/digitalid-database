package net.digitalid.database.dialect.expression.bool;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;

/**
 * An SQL expression that compares two strings.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLStringComparisonBooleanExpression extends SQLBooleanExpression, SQLBinaryExpression<SQLComparisonOperator, SQLStringExpression> {}
