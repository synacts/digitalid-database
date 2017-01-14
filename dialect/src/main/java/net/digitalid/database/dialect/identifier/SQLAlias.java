package net.digitalid.database.dialect.identifier;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.column.SQLColumnAlias;
import net.digitalid.database.dialect.identifier.table.SQLTableAlias;

/**
 * An SQL alias.
 * 
 * @see SQLColumnAlias
 * @see SQLTableAlias
 */
@Immutable
public interface SQLAlias extends SQLIdentifier {}
