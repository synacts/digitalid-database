package net.digitalid.database.dialect.identifier.table;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.SQLName;

/**
 * An SQL table name.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLTableName extends SQLTable, SQLName {}
