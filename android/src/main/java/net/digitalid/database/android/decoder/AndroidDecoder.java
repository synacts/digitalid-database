package net.digitalid.database.android.decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.contracts.Ensure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

import android.database.Cursor;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class AndroidDecoder extends SQLDecoder {
    
    private final @Nonnull Cursor cursor;
    
    private int columnIndex;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected AndroidDecoder(@Nonnull Cursor cursor) {
        this.cursor = cursor;
        this.columnIndex = 0;
    }
    
    /* -------------------------------------------------- Move -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean moveToNextRow() throws DatabaseException {
        final boolean hasNextRow = cursor.moveToNext();
        columnIndex = 0;
        return hasNextRow;
    }
    
    @Impure
    @Override
    public void moveToFirstRow() throws DatabaseException {
        cursor.moveToFirst();
    }
    
    /* -------------------------------------------------- Closing -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException {
        cursor.close();
    }
    
    /* -------------------------------------------------- Nullness -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean wasNull() throws DatabaseException {
        return cursor.isNull(columnIndex++);
    }
    
    /* -------------------------------------------------- Decoding -------------------------------------------------- */
    
    @Impure
    @Override
    public boolean decodeBoolean() throws DatabaseException {
        return cursor.getShort(columnIndex++) != 0;
    }
    
    @Impure
    @Override
    public byte decodeInteger08() throws DatabaseException {
        final short number = cursor.getShort(columnIndex++);
        Ensure.that(number <= Math.pow(2, 8)).orThrow("The number is not byte");
        return (byte) number;
    }
    
    @Impure
    @Override
    public short decodeInteger16() throws DatabaseException {
        return cursor.getShort(columnIndex++);
    }
    
    @Impure
    @Override
    public int decodeInteger32() throws DatabaseException {
        return cursor.getInt(columnIndex++);
    }
    
    @Impure
    @Override
    public long decodeInteger64() throws DatabaseException {
        return cursor.getLong(columnIndex++);
    }
    
    @Impure
    @Override
    public @Nonnull BigInteger decodeInteger() throws DatabaseException {
        final byte[] bytes = cursor.getBlob(columnIndex++);
        return new BigInteger(bytes);
    }
    
    @Impure
    @Override
    public float decodeDecimal32() throws DatabaseException {
        return cursor.getFloat(columnIndex++);
    }
    
    @Impure
    @Override
    public double decodeDecimal64() throws DatabaseException {
        return cursor.getDouble(columnIndex++);
    }
    
    @Impure
    @Override
    public char decodeString01() throws DatabaseException {
        final @Nonnull String character = cursor.getString(columnIndex++);
        Ensure.that(character.length() == 1).orThrow("The value is not a character.");
        return character.charAt(0);
    }
    
    @Impure
    @Override
    public @Nonnull String decodeString64() throws DatabaseException {
        final @Nonnull String string = cursor.getString(columnIndex++);
        Ensure.that(string.length() <= 64).orThrow("The value is not a string of length <= 64.");
        return string;
    }
    
    @Impure
    @Override
    public @Nonnull String decodeString() throws DatabaseException {
        return cursor.getString(columnIndex++);
    }
    
    @Impure
    @Override
    public @Nonnull @Size(16) byte[] decodeBinary128() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        Ensure.that(bytes.length <= 16).orThrow("The value is not a byte array of length <= 16.");
        return bytes;
    }
    
    @Impure
    @Override
    public @Nonnull @Size(32) byte[] decodeBinary256() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        Ensure.that(bytes.length <= 32).orThrow("The value is not a byte array of length <= 32.");
        return bytes;
    }
    
    @Impure
    @Override
    public @Nonnull byte[] decodeBinary() throws DatabaseException {
        return cursor.getBlob(columnIndex++);
    }
    
    @Impure
    @Override
    public @Nonnull InputStream decodeBinaryStream() throws DatabaseException {
        final @Nonnull byte[] bytes = cursor.getBlob(columnIndex++);
        final @Nonnull ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return stream;
    }
    
}
