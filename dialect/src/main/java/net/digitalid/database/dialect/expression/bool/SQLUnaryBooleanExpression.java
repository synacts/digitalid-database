package net.digitalid.database.dialect.expression.bool;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLUnaryExpression;

/**
 * This class implements a unary boolean expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLUnaryBooleanExpression extends SQLBooleanExpression, SQLUnaryExpression<SQLUnaryBooleanOperator, SQLBooleanExpression> {}
