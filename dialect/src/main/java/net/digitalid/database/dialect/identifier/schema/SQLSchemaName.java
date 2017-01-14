package net.digitalid.database.dialect.identifier.schema;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.SQLName;

/**
 * An SQL schema name.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLSchemaName extends SQLName {}
