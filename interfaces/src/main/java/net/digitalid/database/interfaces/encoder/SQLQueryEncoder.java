package net.digitalid.database.interfaces.encoder;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

/**
 * The SQL query encoder helps to prepare statements that are considered to query the data set of
 * the SQL database. It executes SELECT statements.
 * 
 * We treat the SELECT statement different from the Data Manipulation Language (DML) encoder, 
 * because the return value of the execute method is different and, technically, SELECT
 * does not manipulate the data set of the database (even though SELECT is defined as a DML statement).
 * 
 * @see SQLDataManipulationLanguageEncoder
 */
public interface SQLQueryEncoder extends SQLEncoder {
    
    /**
     * Executes the SELECT statement.
     */
    @PureWithSideEffects
    public @Nonnull SQLDecoder execute() throws DatabaseException;
    
}
