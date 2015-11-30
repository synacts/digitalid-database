package net.digitalid.database.core.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.CorruptStateException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.utility.annotations.reference.Capturable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.converter.IterableConverter;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * An SQL converter allows to store and restore objects into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 */
@Immutable
public abstract class AbstractSQLConverter<O, E> {
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Stores the declaration of this SQL converter.
     */
    private final @Nonnull Declaration declaration;
    
    /**
     * Returns the declaration of this SQL converter.
     * 
     * @return the declaration of this SQL converter.
     */
    @Pure
    public final @Nonnull Declaration getDeclaration() {
        return declaration;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL converter with the given declaration.
     * 
     * @param declaration the declaration of the new SQL converter.
     */
    protected AbstractSQLConverter(@Nonnull Declaration declaration) {
        this.declaration = declaration;
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    /**
     * Stores the value of the given non-nullable object for each column in the given array.
     * 
     * @param object the object whose values are to be stored in the values array.
     * @param values a mutable array which stores the value of the object for each column.
     * @param index the current index into the values array.
     * 
     * @require values.size() >= index.getValue() + getDeclaration().getNumberOfColumns() : "The array has enough space for each column.";
     */
    public abstract void storeNonNullable(@Nonnull O object, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> values, @Nonnull MutableIndex index);
    
    /**
     * Stores the value of the given nullable object for each column in the given array.
     * 
     * @param object the object whose values are to be stored in the values array.
     * @param values a mutable array which stores the value of the object for each column.
     * @param index the current index into the values array.
     * 
     * @require values.size() >= index.getValue() + getDeclaration().getNumberOfColumns() : "The array has enough space for each column.";
     */
    public final void storeNullable(@Nullable O object, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> values, @Nonnull MutableIndex index) {
        if (object == null) {
            final int limit = index.getValue() + declaration.getNumberOfColumns();
            while (index.getValue() < limit) { values.set(index.getAndIncrementValue(), "NULL"); }
        } else {
            storeNonNullable(object, values, index);
        }
    }
    
    /**
     * Returns the value of the given object or 'NULL' for each column.
     * 
     * @param object the nullable object whose values are to be returned.
     * 
     * @return the value of the given object or 'NULL' for each column.
     * 
     * @ensure return.size() == getDeclaration().getNumberOfColumns() : "The returned array contains a value for each column.";
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getValues(@Nullable O object) {
        final @Nonnull FreezableArray<String> values = FreezableArray.get(declaration.getNumberOfColumns());
        storeNullable(object, values, MutableIndex.get());
        return values;
    }
    
    // Most of this section will hopefully soon be replaced by the SQL builder.
    
    /**
     * Returns the values of the given object separated by commas.
     * 
     * @param object the object whose values are to be returned.
     * 
     * @return the values of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getInsertForStatement(@Nullable O object) {
        return IterableConverter.toString(getValues(object));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @param object the object whose values are to be used for equality.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @ensure return.size() == getDeclaration().getNumberOfColumns() : "The returned array contains an entry for each column.";
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnsEqualValues(@Nullable O object, @Nullable @Validated String alias) {
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> names = declaration.getColumnNames(alias);
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> values = getValues(object);
        for (int i = 0; i < names.size(); i++) {
            names.set(i, names.getNonNullable(i) + " = " + values.getNonNullable(i)); 
        }
        return names;
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForStatement(@Nullable O object) {
        return IterableConverter.toString(getColumnsEqualValues(object, null));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForStatement(@Nullable O object, @Nullable @Validated String alias) {
        return IterableConverter.toString(getColumnsEqualValues(object, alias), " AND ");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForStatement(@Nullable O object) {
        return getConditionForStatement(object, null);
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given non-nullable object.
     * The number of parameters that are set is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public abstract void storeNonNullable(@Nonnull O object, @Nonnull PreparedStatement preparedStatement, @Nonnull MutableIndex parameterIndex) throws FailedValueStoringException;
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given nullable object.
     * The number of parameters that are set is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param object the nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public final void storeNullable(@Nullable O object, @Nonnull PreparedStatement preparedStatement, @Nonnull MutableIndex parameterIndex) throws FailedValueStoringException {
        if (object == null) { declaration.storeNull(preparedStatement, parameterIndex); }
        else { AbstractSQLConverter.this.storeNonNullable(object, preparedStatement, parameterIndex); }
    }
    
    /* -------------------------------------------------- Restoring -------------------------------------------------- */
    
    /**
     * Returns a nullable object from the given columns of the result set.
     * The number of columns that are read is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param external the external object which is needed to recover the object.
     * @param resultSet the result set from which the data is to be retrieved.
     * @param columnIndex the starting index of the columns containing the data.
     * 
     * @return a nullable object from the given columns of the result set.
     */
    @Pure
    @NonCommitting
    public abstract @Nullable O restoreNullable(@Nonnull E external, @Nonnull ResultSet resultSet, @Nonnull MutableIndex columnIndex) throws FailedValueRestoringException, CorruptStateException, InternalException;
    
    /**
     * Returns a non-nullable object from the given columns of the result set.
     * The number of columns that are read is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param external the external object which is needed to recover the object.
     * @param resultSet the result set from which the data is to be retrieved.
     * @param columnIndex the starting index of the columns containing the data.
     * 
     * @return a non-nullable object from the given columns of the result set.
     */
    @Pure
    @NonCommitting
    public final @Nonnull O restoreNonNullable(@Nonnull E external, @Nonnull ResultSet resultSet, @Nonnull MutableIndex columnIndex) throws FailedValueRestoringException, CorruptStateException, InternalException {
        final @Nullable O object = restoreNullable(external, resultSet, columnIndex);
        if (object == null) { throw CorruptNullValueException.get(); }
        return object;
    }
    
}
