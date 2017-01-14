package net.digitalid.database.interfaces;


import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * An SQL decoder decodes values from an SQL result set.
 * 
 * @see SQLEncoder
 */
@Mutable
public interface SQLDecoder extends AutoCloseable, Decoder<DatabaseException> {
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    /**
     * Moves the cursor to the next row of the selection result.
     * 
     * @return whether there was another row to which the cursor could be moved.
     */
    @Impure
    public boolean moveToNextRow() throws DatabaseException;
    
    /**
     * Moves the cursor to the first row of the selection result.
     * 
     * @throws EntryNotFoundException if the selection results contains no rows.
     */
    @Impure
    public void moveToFirstRow() throws DatabaseException;
    
    /**
     * Returns the current position of the cursor in the columns.
     */
    @Pure
    public int getColumnIndex();
    
    /**
     * Moves the cursor to the column specified in the parameter.
     * 
     * @param columnIndex the column to which the cursor should be moved.
     */
    @Impure
    public void moveToColumn(int columnIndex);
    
    @Impure
    public void moveToFirstColumn();
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Returns whether the last returned column was null.
     */
    @Pure
    public boolean wasNull() throws DatabaseException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException;
    
}
