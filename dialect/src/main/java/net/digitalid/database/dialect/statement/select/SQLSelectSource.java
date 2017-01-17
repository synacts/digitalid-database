package net.digitalid.database.dialect.statement.select;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This node allows to have a select statement as a source of a select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLSelectSource extends SQLSource<SQLSelectStatement> {}
