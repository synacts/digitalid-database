package net.digitalid.utility.database.declaration;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.database.converter.AbstractSQLConverter;

/**
 * This class models a database declaration, specifying the converter, the used prefix and whether the columns are nullable and indexed.
 */
@Immutable
public final class DatabaseDeclaration {
    
    /* -------------------------------------------------- Converter -------------------------------------------------- */
    
    /**
     * Stores the converter used to store and restore the objects.
     */
    private final @Nonnull AbstractSQLConverter<?, ?> converter;
    
    /**
     * Returns the converter used to store and restore the objects.
     * 
     * @return the converter used to store and restore the objects.
     */
    @Pure
    public final @Nonnull AbstractSQLConverter<?, ?> getConverter() {
        return converter;
    }
    
    /* -------------------------------------------------- Nullable -------------------------------------------------- */
    
    /**
     * Stores whether the objects of the converter are nullable.
     */
    private final boolean nullable;
    
    /**
     * Returns whether the objects of the converter are nullable.
     * 
     * @return whether the objects of the converter are nullable.
     */
    @Pure
    public final boolean isNullable() {
        return nullable;
    }
    
    /* -------------------------------------------------- Prefix -------------------------------------------------- */
    
    /**
     * Stores the prefix that is prepended to all column names.
     */
    private final @Nonnull @Validated String prefix;
    
    /**
     * Returns the prefix that is prepended to all column names.
     * 
     * @return the prefix that is prepended to all column names.
     */
    @Pure
    public final @Nonnull @Validated String getPrefix() {
        return prefix;
    }
    
    /* -------------------------------------------------- Indexed -------------------------------------------------- */
    
    /**
     * Stores whether the columns of the converter are indexed.
     */
    private final boolean indexed;
    
    /**
     * Returns whether the columns of the converter are indexed.
     * 
     * @return whether the columns of the converter are indexed.
     */
    @Pure
    public final boolean isIndexed() {
        return indexed;
    }
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Creates a new database declaration with the given parameters.
     * 
     * @param converter the converter used to store and restore the objects.
     * @param nullable whether the objects of the converter are nullable.
     * @param prefix the prefix that is prepended to all column names.
     * @param indexed whether the columns of the converter are indexed.
     */
    private DatabaseDeclaration(@Nonnull AbstractSQLConverter<?, ?> converter, boolean nullable, @Nonnull @Validated String prefix, boolean indexed) {
        this.converter = converter;
        this.nullable = nullable;
        this.prefix = prefix;
        this.indexed = indexed;
    }
    
    /**
     * Creates a new database declaration with the given parameters.
     * 
     * @param converter the converter used to store and restore the objects.
     * @param nullable whether the objects of the converter are nullable.
     * @param prefix the prefix that is prepended to all column names.
     * @param indexed whether the columns of the converter are indexed.
     * 
     * @return a new database declaration with the given parameters.
     */
    @Pure
    public static @Nonnull DatabaseDeclaration get(@Nonnull AbstractSQLConverter<?, ?> converter, boolean nullable, @Nonnull @Validated String prefix, boolean indexed) {
        return new DatabaseDeclaration(converter, nullable, prefix, indexed);
    }
    
    /**
     * Creates a new database declaration with the given parameters.
     * 
     * @param converter the converter used to store and restore the objects.
     * @param nullable whether the objects of the converter are nullable.
     * @param prefix the prefix that is prepended to all column names.
     * 
     * @return a new database declaration with the given parameters.
     */
    @Pure
    public static @Nonnull DatabaseDeclaration get(@Nonnull AbstractSQLConverter<?, ?> converter, boolean nullable, @Nonnull @Validated String prefix) {
        return get(converter, nullable, prefix, false);
    }
    
    /**
     * Creates a new database declaration with the given parameters.
     * 
     * @param converter the converter used to store and restore the objects.
     * @param nullable whether the objects of the converter are nullable.
     * 
     * @return a new database declaration with the given parameters.
     */
    @Pure
    public static @Nonnull DatabaseDeclaration get(@Nonnull AbstractSQLConverter<?, ?> converter, boolean nullable) {
        return get(converter, nullable, "", false);
    }
    
}
