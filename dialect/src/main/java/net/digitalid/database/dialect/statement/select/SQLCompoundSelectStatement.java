package net.digitalid.database.dialect.statement.select;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryExpression;

/**
 * A compound select statement allows to combine and intersect selection results.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLCompoundSelectStatement extends SQLUnorderedSelectStatement, SQLBinaryExpression<SQLCompoundOperator, SQLUnorderedSelectStatement> {}
