package net.digitalid.utility.database.converter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.Capturable;
import net.digitalid.utility.annotations.reference.Captured;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.Frozen;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.converter.ElementConverter;
import net.digitalid.utility.collections.converter.IterableConverter;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.column.ColumnIndex;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.site.Site;

/**
 * A composing SQL converter allows to store and restore objects into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 */
@Immutable
public abstract class ComposingSQLConverter<O, E> extends AbstractSQLConverter<O, E> {
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Stores the converters used to store objects of the class that implements SQL in the database.
     */
    private final @Nonnull @Frozen @NonNullableElements ReadOnlyArray<AbstractSQLConverter<?, ?>> converters;
    
    /**
     * Returns the converters used to store objects of the class that implements SQL in the database.
     * 
     * @return the converters used to store objects of the class that implements SQL in the database.
     */
    @Pure
    public final @Nonnull @Frozen @NonNullableElements ReadOnlyArray<AbstractSQLConverter<?, ?>> getConverters() {
        return converters;
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Stores the number of columns used to store objects of the class that implements SQL in the database.
     */
    private final int numberOfColumns;
    
    @Pure
    @Override
    public final int getNumberOfColumns() {
        return numberOfColumns;
    }
    
    /**
     * Stores the maximum length of the column names.
     */
    private final int maximumColumnNameLength;
    
    @Pure
    @Override
    public final int getMaximumColumnNameLength() {
        return maximumColumnNameLength;
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getDeclaration(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return IterableConverter.toString(converters, new ElementConverter<AbstractSQLConverter<?, ?>>() { 
            @Pure @Override public String toString(@Nullable AbstractSQLConverter<?, ?> converter) { 
                assert converter != null : "The converter is not null.";
                
                return converter.getDeclaration(prefix);
            } 
        });
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getSelection(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return IterableConverter.toString(converters, new ElementConverter<AbstractSQLConverter<?, ?>>() {
            @Pure @Override public String toString(@Nullable AbstractSQLConverter<?, ?> converter) {
                assert converter != null : "The converter is not null.";
                
                return converter.getSelection(prefix);
            }
        });
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public @Nonnull String getForeignKeys(@Nonnull @Validated String prefix, @Nonnull Site site) throws SQLException {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        final @Nonnull StringBuilder string = new StringBuilder();
        // Cannot use IterableConverter.toString() here because the getForeignKeys() method must throw an SQLException.
        for (@Nonnull AbstractSQLConverter<?, ?> converter : converters) {
            string.append(converter.getForeignKeys(prefix, site));
        }
        return string.toString();
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */

    protected final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNames(@Nonnull @Validated String alias, @Nonnull @Validated String prefix) {
        final @Nonnull FreezableArray<String>[] names = (FreezableArray<String>[]) new Object[converters.size()];
        int i = 0;
        for (@Nonnull AbstractSQLConverter<?, ?> converter : converters) {
            names[i++] = converter.getColumnNames(alias, prefix);
        }
        return FreezableArray.getNonNullable(names);
    }

    @Override
    public final void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException {
        for (AbstractSQLConverter<?, ?> converter : converters) {
            converter.storeNull(preparedStatement, parameterIndex);
        }
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new composing SQL converter with the given SQL converters.
     * 
     * @param converters the converters used to store objects of the class that implements SQL.
     * 
     * @return a new composing SQL converter with the given SQL converters.
     */
    @SafeVarargs
    protected ComposingSQLConverter(@Captured @Nonnull AbstractSQLConverter<?, ?>... converters) {
        this.converters = FreezableArray.getNonNullable(converters).freeze();
        int numberOfColumns = 0;
        for (@Nonnull AbstractSQLConverter<?, ?> converter : converters) {
            numberOfColumns += converter.getNumberOfColumns();
        }
        this.numberOfColumns = numberOfColumns;
        
        int maximumColumnNameLength = 0;
        for (@Nonnull AbstractSQLConverter<?, ?> converter : converters) {
            final int columnNameLength = converter.getMaximumColumnNameLength();
            if (columnNameLength > maximumColumnNameLength) maximumColumnNameLength = columnNameLength;
        }
        this.maximumColumnNameLength = maximumColumnNameLength;
    }
    
}
