package net.digitalid.database.dialect.statement;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * An SQL statement.
 * 
 * @see SQLTableStatement
 * @see SQLSelectStatement
 */
@Immutable
public interface SQLStatement extends SQLNode {}
