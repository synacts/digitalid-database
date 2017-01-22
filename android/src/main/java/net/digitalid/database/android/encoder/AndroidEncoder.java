package net.digitalid.database.android.encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.exceptions.UncheckedExceptionBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.Size;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

import android.content.ContentValues;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public class AndroidEncoder extends SQLEncoder {
    
    private final @Nonnull ContentValues contentValues;
    
    private int parameterIndex;
    
    private final @Nonnull String[] columnNames;
    
    protected AndroidEncoder(@Nonnull ContentValues contentValues, @Nonnull String[] columnNames) {
        this.contentValues = contentValues;
        this.columnNames = columnNames;
        this.parameterIndex = 0;
    }
    
    /* -------------------------------------------------- SQL Encoder -------------------------------------------------- */
    
    @Impure
    @Override
    public void close() throws DatabaseException {
    }
    
    @Impure
    @Override
    public void encodeNull(int typeCode) throws DatabaseException {
        contentValues.putNull(columnNames[parameterIndex++]);
    }
    
    @Impure
    @Override
    public void encodeBoolean(boolean value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeInteger08(byte value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeInteger16(short value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeInteger32(int value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeInteger64(long value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeInteger(@Nonnull BigInteger value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value.toByteArray());
    }
    
    @Impure
    @Override
    public void encodeDecimal32(float value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeDecimal64(double value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeString01(char value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], String.valueOf(value));
    }
    
    @Impure
    @Override
    public void encodeString64(@Nonnull @MaxSize(64) String value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeString(@Nonnull String value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeBinary128(@Nonnull @Size(16) byte[] value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeBinary256(@Nonnull @Size(32) byte[] value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeBinary(@Nonnull byte[] value) throws DatabaseException {
        contentValues.put(columnNames[parameterIndex++], value);
    }
    
    @Impure
    @Override
    public void encodeBinaryStream(@Nonnull InputStream stream, int length) throws DatabaseException {
        final @Nonnull ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while (stream.available() > 0) {
                bos.write(stream.read());
            }
        } catch (IOException exception) {
            // TODO: is this the correct exception?
            throw UncheckedExceptionBuilder.withCause(exception).build();
        }
        contentValues.put(columnNames[parameterIndex++], bos.toByteArray());
    }
    
}
