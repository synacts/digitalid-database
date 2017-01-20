package net.digitalid.database.interfaces.encoder;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

/**
 * An SQL encoder encodes values to an SQL statement.
 * 
 * @see SQLDecoder
 */
@Mutable
public interface SQLEncoder extends AutoCloseable, Encoder<DatabaseException> {
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
     * 
     * @param typeCode the SQL type of the next parameter which is to be set to null.
     */
    @Impure
    public @Nonnull Integer encodeNull(int typeCode) throws DatabaseException;
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    /**
     * Adds the current values to the batch of values which are to be inserted.
     */
    @Impure
    public void addBatch() throws DatabaseException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException;
    
}
