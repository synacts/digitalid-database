package net.digitalid.database.dialect.statement;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.statement.schema.SQLCreateSchemaStatement;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * An SQL statement node.
 * 
 * @see SQLTableStatement
 * @see SQLSelectStatement
 * @see SQLCreateSchemaStatement
 */
@Immutable
public interface SQLStatementNode extends SQLNode {}
