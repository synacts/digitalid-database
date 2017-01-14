package net.digitalid.database.dialect.identifier.column;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;

/**
 * An SQL column.
 * 
 * @see SQLColumnName
 * @see SQLColumnAlias
 * @see SQLQualifiedColumn
 */
@Immutable
public interface SQLColumn extends SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {}
