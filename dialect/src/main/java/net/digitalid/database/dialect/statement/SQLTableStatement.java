package net.digitalid.database.dialect.statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.statement.delete.SQLDeleteStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.drop.SQLDropTableStatement;

/**
 * An SQL statement that operates on a table.
 * 
 * @see SQLDeleteStatement
 * @see SQLInsertStatement
 * @see SQLCreateTableStatement
 * @see SQLDropTableStatement
 */
@Immutable
public interface SQLTableStatement extends SQLStatement {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table on which the statement operates.
     */
    @Pure
    public @Nonnull SQLQualifiedTable getTable();
    
}
