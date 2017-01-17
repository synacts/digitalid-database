package net.digitalid.database.dialect.statement.select;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.dialect.statement.SQLStatement;

/**
 * This SQL node represents an SQL select statement.
 * 
 * @see SQLOrderedSelectStatement
 * @see SQLUnorderedSelectStatement
 */
@Immutable
public interface SQLSelectStatement extends SQLStatement, SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {}
