package net.digitalid.database.dialect.identifier;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * An SQL prefix.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLPrefix extends SQLIdentifier {}
