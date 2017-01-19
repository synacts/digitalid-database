package net.digitalid.database.interfaces;

import java.sql.Types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.immutable.ImmutableMap;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * An SQL encoder encodes values to an SQL statement.
 * 
 * @see SQLDecoder
 */
@Mutable
@TODO(task = "Check ultimately whether it makes sense that the SQLEncoder extends AutoCloseable.", date = "2017-01-19", author = Author.KASPAR_ETTER)
public interface SQLEncoder extends AutoCloseable, Encoder<DatabaseException> {
    
    /* -------------------------------------------------- Null -------------------------------------------------- */
    
    /**
     * Sets the next parameter of the given SQL type to null.
=     */
    @Impure
    public void encodeNull(int typeCode) throws DatabaseException;
    
    /**
     * Stores the mapping from custom types to SQL types.
     */
    public static @Nonnull ImmutableMap<@Nonnull CustomType, @Nonnull Integer> typeMapping = ImmutableMap
            .with(CustomType.BOOLEAN, Types.BOOLEAN)
            .with(CustomType.INTEGER08, Types.TINYINT)
            .with(CustomType.INTEGER16, Types.SMALLINT)
            .with(CustomType.INTEGER32, Types.INTEGER)
            .with(CustomType.INTEGER64, Types.BIGINT)
            .with(CustomType.INTEGER, Types.BLOB)
            .with(CustomType.DECIMAL32, Types.FLOAT)
            .with(CustomType.DECIMAL64, Types.DOUBLE)
            .with(CustomType.STRING01, Types.CHAR)
            .with(CustomType.STRING64, Types.VARCHAR)
            .with(CustomType.STRING, Types.VARCHAR)
            .with(CustomType.BINARY128, Types.BINARY)
            .with(CustomType.BINARY256, Types.BINARY)
            .with(CustomType.BINARY, Types.BLOB)
            .build();
    
    /**
     * Returns the SQL type that corresponds to the given custom type.
     */
    @Pure
    public static int getSQLType(@Nonnull CustomType customType) {
        final @Nullable Integer result = typeMapping.get(customType);
        if (result == null) { throw CaseExceptionBuilder.withVariable("customType").withValue(customType).build(); }
        else { return result; }
    }
    
    /**
     * Sets the next parameter of the given custom type to null.
=     */
    @Impure
    public default void encodeNull(@Nonnull CustomType customType) throws DatabaseException {
        encodeNull(getSQLType(customType));
    }
    
    /* -------------------------------------------------- Batching -------------------------------------------------- */
    
    /**
     * Adds the current values to the batch of values which are to be inserted.
     */
    @Impure
    @TODO(task = "Maybe move this to an SQLInsertStatementEncoder or something similar.", date = "2017-01-19", author = Author.KASPAR_ETTER)
    public void addBatch() throws DatabaseException;
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException;
    
}
