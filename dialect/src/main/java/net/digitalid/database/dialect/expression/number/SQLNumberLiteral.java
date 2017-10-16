package net.digitalid.database.dialect.expression.number;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLLiteral;

/**
 * A number literal.
 * 
 * @see SQLLongLiteral
 * @see SQLDoubleLiteral
 */
@Immutable
public interface SQLNumberLiteral extends SQLNumberExpression, SQLLiteral {}
