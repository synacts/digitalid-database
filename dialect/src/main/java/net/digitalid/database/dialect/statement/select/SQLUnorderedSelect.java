package net.digitalid.database.dialect.statement.select;

import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;

/**
 * Creates an SQL select statement node without ORDER BY clause.
 * 
 * @see SQLSimpleSelect
 * @see SQLCompoundSelect
 */
@Immutable
public interface SQLUnorderedSelect extends SQLNode {
    
    /* -------------------------------------------------- Compound Operations -------------------------------------------------- */
    
    
    
}
