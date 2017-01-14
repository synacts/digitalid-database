package net.digitalid.database.dialect.identifier.column;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.SQLAlias;

/**
 * An SQL column alias.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLColumnAlias extends SQLColumn, SQLAlias {}
