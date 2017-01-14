package net.digitalid.database.dialect.identifier.table;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;

/**
 * An SQL column.
 * 
 * @see SQLTableName
 * @see SQLTableAlias
 * @see SQLQualifiedTable
 */
@Immutable
public interface SQLTable extends SQLNode {}
