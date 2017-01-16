package net.digitalid.database.dialect.statement.insert;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * SQL values.
 * 
 * @see SQLRows
 * @see SQLSelectStatement
 */
@Immutable
public interface SQLValues extends SQLNode {}
