package net.digitalid.utility.database.converter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.NonCapturable;
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
import net.digitalid.utility.collections.tuples.ReadOnlyPair;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.column.ColumnIndex;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.site.Site;

/**
 * A composing SQL converter allows to store and restore objects based on other SQL converters into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 */
@Immutable
public abstract class ComposingSQLConverter<O, E> extends AbstractSQLConverter<O, E> {
    
    // TODO: Introduce an IndexingSQLConverter that extends this class similarly to the ChainingSQLConverter.
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    // TODO: One should also be able to specify prefixes here!
    
    // TODO: Replace the ReadOnlyPair with something more specific like DatabaseDeclaration.
    
    /**
     * Stores the converters used to store the fields of an object of the generic type {@link O} and their nullability.
     */
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean>> converters;
    
    /**
     * Returns the converters used to store the fields of an object of the generic type {@link O} and their nullability.
     * 
     * @return the converters used to store the fields of an object of the generic type {@link O} and their nullability.
     */
    @Pure
    public final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean>> getConverters() {
        return converters;
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Stores the number of columns used to store an object of the generic type {@link O}.
     */
    private final int numberOfColumns;
    
    @Pure
    @Override
    public final int getNumberOfColumns() {
        return numberOfColumns;
    }
    
    /**
     * Stores the length of the longest column name.
     */
    private final int lengthOfLongestColumnName;
    
    @Pure
    @Override
    public final int getLengthOfLongestColumnName() {
        return lengthOfLongestColumnName;
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getDeclaration(final boolean nullable, final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return IterableConverter.toString(converters, new ElementConverter<ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean>>() { 
            @Pure @Override public String toString(@Nullable ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter) { 
                assert converter != null : "The converter is not null.";
                
                return converter.getNonNullableElement0().getDeclaration(nullable || converter.getNonNullableElement1(), prefix); // TODO: Include the declaration-specific prefix as well.
            } 
        });
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getSelection(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return IterableConverter.toString(converters, new ElementConverter<ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean>>() {
            @Pure @Override public String toString(@Nullable ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter) {
                assert converter != null : "The converter is not null.";
                
                return converter.getNonNullableElement0().getSelection(prefix);
            }
        });
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public @Nonnull String getForeignKeys(@Nonnull Site site, @Nonnull @Validated String prefix) throws SQLException {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        final @Nonnull StringBuilder string = new StringBuilder();
        // Cannot use IterableConverter.toString() here because the getForeignKeys() method can throw an SQLException.
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            string.append(converter.getNonNullableElement0().getForeignKeys(site, prefix));
        }
        return string.toString();
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    @SuppressWarnings("unchecked")
    public void executeAfterCreation(@Nonnull @NonNullableElements @Frozen ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, String>... convertersOfSameUniqueConstraint) throws SQLException {
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            converter.getNonNullableElement0().executeAfterCreation(convertersOfSameUniqueConstraint);
        }
    }
    
    @Locked
    @Override
    @NonCommitting
    @SuppressWarnings("unchecked")
    public void executeBeforeDeletion(@Nonnull @NonNullableElements @Frozen ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, String>... convertersOfSameUniqueConstraint) throws SQLException {
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            converter.getNonNullableElement0().executeBeforeDeletion(convertersOfSameUniqueConstraint);
        }
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    @Override
    protected final void getColumnNames(@Nonnull @Validated String alias, @Nonnull @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull ColumnIndex index) {
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            converter.getNonNullableElement0().getColumnNames(alias, prefix, names, index);
        }
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException {
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            converter.getNonNullableElement0().storeNull(preparedStatement, parameterIndex);
        }
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new composing SQL converter with the given SQL converters and their nullability.
     * 
     * @param converters the converters used to store the fields of an object of the generic type {@link O} and their nullability.
     * 
     * @return a new composing SQL converter with the given SQL converters and their nullability.
     */
    @SafeVarargs
    protected ComposingSQLConverter(@Nonnull @NonNullableElements @Frozen ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean>... converters) {
        this.converters = FreezableArray.getNonNullable(converters).freeze();
        
        int numberOfColumns = 0;
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            numberOfColumns += converter.getNonNullableElement0().getNumberOfColumns();
        }
        this.numberOfColumns = numberOfColumns;
        
        int lengthOfLongestColumnName = 0;
        for (@Nonnull ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, Boolean> converter : converters) {
            final int columnNameLength = converter.getNonNullableElement0().getLengthOfLongestColumnName();
            if (columnNameLength > lengthOfLongestColumnName) { lengthOfLongestColumnName = columnNameLength; }
        }
        this.lengthOfLongestColumnName = lengthOfLongestColumnName;
    }
    
}
