package net.digitalid.database.interfaces.encoder;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * The SQL action encoder helps to prepare statements that are considered
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
@Mutable
public interface SQLActionEncoder extends SQLEncoder {
    
    /**
     * Executes the data manipulation language (DML) statement of the type:
     * - INSERT
     * - UPDATE
     * - DELETE
     */
    @PureWithSideEffects
    public abstract void execute() throws DatabaseException;
    
}
