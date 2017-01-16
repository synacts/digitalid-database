package net.digitalid.database.dialect.statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;

/**
 * An SQL statement.
 * 
 * @see SQLDeleteStatement
 * @see SQLInsertStatement
 * @see SQLDropTableStatement
 */
@Immutable
public interface SQLStatement extends SQLNode {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table on which the statement operates.
     */
    @Pure
    public @Nonnull SQLQualifiedTable getTable();
    
}
