package net.digitalid.database.dialect.statement.select.unordered.simple.sources;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;

/**
 * This node allows to have a table as a source of a select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLTableSource extends SQLSource<SQLQualifiedTable> {}
