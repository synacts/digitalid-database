package net.digitalid.database.dialect.identifier.table;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.SQLAlias;

/**
 * An SQL table alias.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLTableAlias extends SQLTable, SQLAlias {}
