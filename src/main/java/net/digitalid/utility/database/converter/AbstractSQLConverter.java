package net.digitalid.utility.database.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.Capturable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.Frozen;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.converter.IterableConverter;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.tuples.ReadOnlyPair;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.column.ColumnIndex;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.site.Site;

/**
 * An SQL converter allows to store and restore objects into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 * @see ColumnSQLConverter
 * @see ComposingSQLConverter
 */
@Immutable
public abstract class AbstractSQLConverter<O, E> {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the number of columns used to store an object of the generic type {@link O}.
     * 
     * @return the number of columns used to store an object of the generic type {@link O}.
     */
    @Pure
    public abstract int getNumberOfColumns();
    
    /**
     * Returns the length of the longest column name.
     * 
     * @return the length of the longest column name.
     */
    @Pure
    public abstract int getLengthOfLongestColumnName();
    
    /* -------------------------------------------------- Prefix -------------------------------------------------- */
    
    /**
     * Returns whether the given prefix is valid.
     * 
     * @param prefix the prefix to be checked.
     * 
     * @return whether the given prefix is valid.
     */
    @Pure
    public final boolean isValidPrefix(@Nonnull String prefix) {
        return prefix.isEmpty() || prefix.length() + getLengthOfLongestColumnName() <= Database.getConfiguration().getMaximumIdentifierLength() - 1 && Database.getConfiguration().isValidIdentifier(prefix);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */

    /**
     * Returns the columns as declaration with the given prefix.
     * 
     * @param nullable whether the stored objects may be null.
     * @param prefix the prefix to prepend to all column names.
     * 
     * @return the columns as declaration with the given prefix.
     */
    @Pure
    public abstract @Nonnull String getDeclaration(boolean nullable, @Nonnull @Validated String prefix);
    
    /**
     * Returns the columns as declaration without a prefix.
     * 
     * @param nullable whether the stored objects may be null.
     * 
     * @return the columns as declaration without a prefix.
     */
    @Pure
    public final @Nonnull String getDeclaration(boolean nullable) {
        return getDeclaration(nullable, "");
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    /**
     * Returns the columns for selection with the given prefix.
     * 
     * @param prefix the prefix to prepend to all column names.
     * 
     * @return the columns for selection with the given prefix.
     */
    @Pure
    public abstract @Nonnull String getSelection(@Nonnull @Validated String prefix);
    
    /**
     * Returns the columns for selection without a prefix.
     * 
     * @return the columns for selection without a prefix.
     */
    @Pure
    public final @Nonnull String getSelection() {
        return getSelection("");
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    /**
     * Returns the foreign key constraints of the columns with the given prefix.
     * 
     * @param site the site at which the foreign key constraints are declared.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the foreign key constraints of the columns with the given prefix.
     * 
     * @ensure return.isEmpty() || return.startsWith(",") : "The returned string is either empty or starts with a comma.";
     */
    @Locked
    @NonCommitting
    public abstract @Nonnull String getForeignKeys(@Nonnull Site site, @Nonnull @Validated String prefix) throws SQLException;
    
    /**
     * Returns the foreign key constraints of the columns without a prefix.
     * 
     * @param site the site at which the foreign key constraints are declared.
     * 
     * @return the foreign key constraints of the columns without a prefix.
     * 
     * @ensure return.isEmpty() || return.startsWith(",") : "The returned string is either empty or starts with a comma.";
     */
    @Locked
    @NonCommitting
    public final @Nonnull String getForeignKeys(@Nonnull Site site) throws SQLException {
        return getForeignKeys(site, "");
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    /**
     * This method is called after the creation of a table on all converters that belong to a unique constraint.
     * (The identity converter overrides this method to create indexes on identity columns and register them at the mapper.)
     * 
     * @param convertersOfSameUniqueConstraint all converters of the same unique constraint as this converter (including this one) and their prefixes.
     */
    @Locked
    @NonCommitting
    @SuppressWarnings("unchecked")
    public void executeAfterCreation(@Nonnull @NonNullableElements @Frozen ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, String>... convertersOfSameUniqueConstraint) throws SQLException {}
    
    /**
     * This method is called before the deletion of a table on all converters that belong to a unique constraint.
     * (The identity converter overrides this method to delete indexes on identity columns and deregister them at the mapper.)
     * 
     * @param convertersOfSameUniqueConstraint all converters of the same unique constraint as this converter (including this one) and their prefixes.
     */
    @Locked
    @NonCommitting
    @SuppressWarnings("unchecked")
    public void executeBeforeDeletion(@Nonnull @NonNullableElements @Frozen ReadOnlyPair<? extends AbstractSQLConverter<?, ?>, String>... convertersOfSameUniqueConstraint) throws SQLException {}
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    /**
     * Stores the value of the given object for each column in the given array.
     * 
     * @param object the object whose values are to be returned.
     * @param values a mutable array which stores the value of the object for each column.
     * @param index the current index into the values array.
     * 
     * @require values.size() == getNumberOfColumns() : "The array contains a value for each column.";
     * 
     * @ensure !values.containsNull() : "After the execution of this method, the array of values does not contain null.";
     */
    public abstract void getValues(@Nonnull O object, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> values, @Nonnull ColumnIndex index);
    
    /**
     * Returns the value of the given object or 'NULL' for each column.
     * 
     * @param object the nullable object whose values are to be returned.
     * 
     * @return the value of the given object or 'NULL' for each column.
     * 
     * @ensure return.size() == getNumberOfColumns() : "The returned array contains a value for each column.";
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getValuesOrNulls(@Nullable O object) {
        final @Nonnull FreezableArray<String> values = FreezableArray.get(getNumberOfColumns());
        if (object == null) {
            return values.setAll("NULL");
        } else {
            getValues(object, values, ColumnIndex.get());
            return values;
        }
    }
    
    /**
     * Returns the values of the given object separated by commas.
     * 
     * @param object the object whose values are to be returned.
     * 
     * @return the values of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getInsertForStatement(@Nullable O object) {
        return IterableConverter.toString(getValuesOrNulls(object));
    }
    
    /**
     * Returns whether the given alias is valid.
     * 
     * @param alias the alias to be checked.
     * 
     * @return whether the given alias is valid.
     */
    @Pure
    public static boolean isValidAlias(@Nonnull String alias) {
        return alias.isEmpty() || Database.getConfiguration().isValidIdentifier(alias);
    }
    
    /**
     * Stores the name of each column with the given alias and prefix in the given array.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * @param prefix the prefix that is to be prepended to all column names.
     * @param names a mutable array which stores the name of each column.
     * @param index the current index into the names array.
     * 
     * @require names.size() == getNumberOfColumns() : "The array contains a name for each column.";
     * 
     * @ensure !names.containsNull() : "After the execution of this method, the array of names does not contain null.";
     */
    protected abstract void getColumnNames(@Nonnull @Validated String alias, @Nonnull @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull ColumnIndex index);
    
    /**
     * Returns the name of each column with the given alias and prefix.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column with the given alias and prefix.
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNames(@Nonnull @Validated String alias, @Nonnull @Validated String prefix) {
        final @Nonnull FreezableArray<String> names = FreezableArray.get(getNumberOfColumns());
        getColumnNames(alias, prefix, names, ColumnIndex.get());
        return names;
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * @param prefix the prefix that is to be prepended to all column names.
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @ensure return.size() == getNumberOfColumns() : "The returned array contains an entry for each column.";
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnsEqualValues(@Nonnull @Validated String alias, @Nonnull @Validated String prefix, @Nullable O object) {
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> names = getColumnNames(alias, prefix);
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> values = getValuesOrNulls(object);
        for (int i = 0; i < getNumberOfColumns(); i++) {
            names.set(i, names.getNonNullable(i) + " = " + values.getNonNullable(i)); 
        }
        return names;
    }

    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     * 
     * @param object the object whose values are to be used for equality.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForStatement(@Nullable O object, @Nonnull @Validated String prefix) {
        return IterableConverter.toString(getColumnsEqualValues("", prefix, object));
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
        return getUpdateForStatement(object, "");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * @param prefix the prefix that is to be prepended to all column names.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForStatement(@Nullable O object, @Nonnull @Validated String prefix, @Nonnull @Validated String alias) {
        return IterableConverter.toString(getColumnsEqualValues(alias, prefix, object), " AND ");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForStatement(@Nullable O object, @Nonnull @Validated String prefix) {
        return getConditionForStatement(object, prefix, "");
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
        return getConditionForStatement(object, "");
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    /**
     * Returns as many question marks as columns separated by commas.
     * 
     * @return as many question marks as columns separated by commas.
     */
    @Pure
    public final @Nonnull String getInsertForPreparedStatement() {
        return IterableConverter.toString(FreezableArray.<String>get(getNumberOfColumns()).setAll("?"));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark.
     * 
     * @ensure return.size() == getNumberOfColumns() : "The returned array contains an entry for each column.";
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNamesEqualQuestionMarks(@Nonnull @Validated String alias, @Nonnull @Validated String prefix) {
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> names = getColumnNames(alias, prefix);
        for (int i = 0; i < getNumberOfColumns(); i++) {
            names.set(i, names.getNonNullable(i) + " = ?"); 
        }
        return names;
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by commas.
     * 
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForPreparedStatement(@Nonnull @Validated String prefix) {
        return IterableConverter.toString(getColumnNamesEqualQuestionMarks("", prefix));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by commas.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForPreparedStatement() {
        return getUpdateForPreparedStatement("");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     * 
     * @param prefix the prefix that is to be prepended to all column names.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForPreparedStatement(@Nonnull @Validated String prefix, @Nonnull @Validated String alias) {
        return IterableConverter.toString(getColumnNamesEqualQuestionMarks(alias, prefix), " AND ");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     * 
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForPreparedStatement(@Nonnull @Validated String prefix) {
        return getConditionForPreparedStatement(prefix, "");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForPreparedStatement() {
        return getConditionForPreparedStatement("");
    }
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given non-nullable object.
     * The number of parameters that are set is given by {@link #getNumberOfColumns()}.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public abstract void storeNonNullable(@Nonnull O object, @Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException;
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to null.
     * The number of parameters that are set is given by {@link #getNumberOfColumns()}.
     * 
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public abstract void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException;
        
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given nullable object.
     * The number of parameters that are set is given by {@link #getNumberOfColumns()}.
     * 
     * @param object the nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public final void storeNullable(@Nullable O object, @Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException {
        if (object == null) { storeNull(preparedStatement, parameterIndex); }
        else { storeNonNullable(object, preparedStatement, parameterIndex); }
    }
    
    /* -------------------------------------------------- Restoring -------------------------------------------------- */
    
    /**
     * Returns a nullable object from the given columns of the result set.
     * The number of columns that are read is given by {@link #getNumberOfColumns()}.
     * 
     * @param external the external object which is needed to recover the object.
     * @param resultSet the result set from which the data is to be retrieved.
     * @param columnIndex the starting index of the columns containing the data.
     * 
     * @return a nullable object from the given columns of the result set.
     */
    @Pure
    @NonCommitting
    public abstract @Nullable O restoreNullable(@Nonnull E external, @Nonnull ResultSet resultSet, @Nonnull ColumnIndex columnIndex) throws SQLException;
    
    /**
     * Returns a non-nullable object from the given columns of the result set.
     * The number of columns that are read is given by {@link #getNumberOfColumns()}.
     * 
     * @param external the external object which is needed to recover the object.
     * @param resultSet the result set from which the data is to be retrieved.
     * @param columnIndex the starting index of the columns containing the data.
     * 
     * @return a non-nullable object from the given columns of the result set.
     */
    @Pure
    @NonCommitting
    public final @Nonnull O restoreNonNullable(@Nonnull E external, @Nonnull ResultSet resultSet, @Nonnull ColumnIndex columnIndex) throws SQLException {
        final @Nullable O object = restoreNullable(external, resultSet, columnIndex);
        if (object == null) { throw new SQLException("An object which should not be null was null."); }
        return object;
    }
    
}
