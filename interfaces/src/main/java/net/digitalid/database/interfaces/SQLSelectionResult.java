package net.digitalid.database.interfaces;


import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.SelectionResult;

import net.digitalid.database.exceptions.operation.FailedResourceClosingException;
import net.digitalid.database.exceptions.operation.FailedSQLValueRecoveryException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;

/**
 * This interface allows to get the values of an SQL result.
 * Advancing the column index is left to the implementation.
 */
public interface SQLSelectionResult extends AutoCloseable, SelectionResult<FailedSQLValueRecoveryException> {
    
    /* -------------------------------------------------- Iteration -------------------------------------------------- */
    
    /**
     * Moves the cursor to the next row of the selection result.
     * 
     * @return whether there was another row to which the cursor could be moved.
     */
    @Impure
    public boolean moveToNextRow() throws FailedSQLValueRecoveryException;
    
    /**
     * Moves the cursor to the first row of the selection result.
     * 
     * @throws EntryNotFoundException if the selection results contains no rows.
     */
    @Impure
    public void moveToFirstRow() throws FailedSQLValueRecoveryException;
    
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
    public boolean wasNull() throws FailedSQLValueRecoveryException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws FailedResourceClosingException;
    
}
