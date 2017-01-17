package net.digitalid.database.dialect.statement.select.unordered.simple.columns;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;

/**
 * A result column or all columns.
 * 
 * @see SQLResultColumn
 * @see SQLAllColumns
 */
@Immutable
public interface SQLResultColumnOrAllColumns extends SQLNode {}
