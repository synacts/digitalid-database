package net.digitalid.database.interfaces.encoder;

import net.digitalid.utility.annotations.method.PureWithSideEffects;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * The SQL data manipulation language (DML) encoder helps to prepare statements that are considered
 * to manipulate the data set of the SQL database. It can handle the following statements:
 * - INSERT
 * - UPDATE
 * - DELETE
 * Although, by definition, the SELECT statement also falls under the DML category, we treat it
 * separately, because the return value of the execute method is different and, technically, SELECT
 * does not manipulate the data set of the database.
 * 
 * @see SQLQueryEncoder
 */
public interface SQLDataManipulationLanguageEncoder {
    
    /**
     * Executes the data manipulation language (DML) statement of the type:
     * - INSERT
     * - UPDATE
     * - DELETE
     */
    @PureWithSideEffects
    public void execute() throws DatabaseException;
    
}
