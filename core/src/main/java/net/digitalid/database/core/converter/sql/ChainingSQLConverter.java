package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.converter.key.KeyConverter;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptParameterValueException;
import net.digitalid.database.core.exceptions.state.value.CorruptValueException;
import net.digitalid.database.core.exceptions.state.value.MaskingCorruptValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Matching;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.exceptions.external.InvalidEncodingException;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * This class implements an SQL converter that is based on another SQL converter.
 * 
 * @param <O> the type of the objects that this converter can store and restore (typically the surrounding class).
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an object.
 * @param <K> the type of the objects that the other converter stores and restores (as a key for this converter's objects).
 * @param <D> the type of the external object that is needed to recover the key, which is quite often an entity.
 *            In case no external information is needed for the recovery of the key, declare it as an object.
 * 
 * @see RedeclaringSQLConverter
 */
@Immutable
public class ChainingSQLConverter<O, E, K, D> extends SQLConverterOld<O, E> {
    
    /* -------------------------------------------------- Key Converter -------------------------------------------------- */
    
    /**
     * Stores the key converter used to convert and recover the object.
     */
    private final @Nonnull KeyConverter<O, ? super E, K, D> keyConverter;
    
    /**
     * Returns the key converter used to convert and recover the object.
     * 
     * @return the key converter used to convert and recover the object.
     */
    @Pure
    public final @Nonnull KeyConverter<O, ? super E, K, D> getKeyConverter() {
        return keyConverter;
    }
    
    /* -------------------------------------------------- SQL Converter -------------------------------------------------- */
    
    /**
     * Stores the SQL converter used to store and restore the object's key.
     */
    private final @Nonnull
    SQLConverterOld<K, ? super D> SQLConverter;
    
    /**
     * Returns the SQL converter used to store and restore the object's key.
     * 
     * @return the SQL converter used to store and restore the object's key.
     */
    @Pure
    public final @Nonnull
    SQLConverterOld<K, ? super D> getSQLConverter() {
        return SQLConverter;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new chaining SQL converter with the given converters.
     * 
     * @param declaration the declaration of the new chaining SQL converter.
     * @param keyConverter the key converter used to convert and recover the object.
     * @param SQLConverter the SQL converter used to store and restore the object's key.
     */
    protected ChainingSQLConverter(@Nonnull @Matching Declaration declaration, @Nonnull KeyConverter<O, ? super E, K, D> keyConverter, @Nonnull SQLConverterOld<K, ? super D> SQLConverter) {
        super(declaration);
        
        assert declaration.matches(SQLConverter.getDeclaration()) : "The declaration matches the declaration of the SQL converter.";
        
        this.keyConverter = keyConverter;
        this.SQLConverter = SQLConverter;
    }
    
    /**
     * Returns a new chaining SQL converter with the given converters.
     * 
     * @param declaration the declaration of the new chaining SQL converter.
     * @param keyConverter the key converter used to convert and recover the object.
     * @param SQLConverter the SQL converter used to store and restore the object's key.
     * 
     * @return a new chaining SQL converter with the given converters.
     */
    @Pure
    public static @Nonnull <O, E, K, D> ChainingSQLConverter<O, E, K, D> get(@Nonnull @Matching Declaration declaration, @Nonnull KeyConverter<O, ? super E, K, D> keyConverter, @Nonnull SQLConverterOld<K, ? super D> SQLConverter) {
        return new ChainingSQLConverter<>(declaration, keyConverter, SQLConverter);
    }
    
    /**
     * Returns a new chaining SQL converter with the given converters.
     * 
     * @param keyConverter the key converter used to convert and recover the object.
     * @param SQLConverter the SQL converter used to store and restore the object's key.
     * 
     * @return a new chaining SQL converter with the given converters.
     */
    @Pure
    public static @Nonnull <O, E, K, D> ChainingSQLConverter<O, E, K, D> get(@Nonnull KeyConverter<O, ? super E, K, D> keyConverter, @Nonnull SQLConverterOld<K, ? super D> SQLConverter) {
        return new ChainingSQLConverter<>(SQLConverter.getDeclaration(), keyConverter, SQLConverter);
    }
    
    /* -------------------------------------------------- Conversions -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNonNullable(@Nonnull O object, @Nonnull ValueCollector collector) throws FailedValueStoringException {
        SQLConverter.storeNonNullable(keyConverter.convert(object), collector);
    }
    
    @Override
    @NonCommitting
    public final @Nullable O restoreNullable(@Nonnull E external, @NonCapturable @Nonnull SelectionResult result) throws FailedValueRestoringException, CorruptValueException, InternalException {
        final @Nullable K key = SQLConverter.restoreNullable(keyConverter.decompose(external), result);
        if (key == null) { return null; }
        if (!keyConverter.isValid(key)) { throw CorruptParameterValueException.get("key", key); }
        try {
            return keyConverter.recover(external, key);
        } catch (@Nonnull InvalidEncodingException exception) {
            throw MaskingCorruptValueException.get(exception);
        }
    }
    
}
