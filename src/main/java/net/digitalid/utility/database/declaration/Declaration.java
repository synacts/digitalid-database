package net.digitalid.utility.database.declaration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.site.Site;
import net.digitalid.utility.database.table.Table;

/**
 * This class models a database declaration that consists of a certain number of columns.
 * 
 * @see ColumnDeclaration
 * @see ChainingDeclaration
 * @see CombiningDeclaration
 */
@Immutable
public abstract class Declaration {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the number of (unique) columns of this declaration.
     * 
     * @param unique whether only unique columns shall be counted.
     * 
     * @return the number of (unique) columns of this declaration.
     */
    @Pure
    protected abstract int getNumberOfColumns(boolean unique);
    
    /**
     * Returns the number of columns of this declaration.
     * 
     * @return the number of columns of this declaration.
     */
    @Pure
    public final int getNumberOfColumns() {
        return getNumberOfColumns(false);
    }
    
    /**
     * Returns the length of the longest column name.
     * 
     * @return the length of the longest column name.
     */
    @Pure
    public abstract int getLengthOfLongestColumnName();
    
    /* -------------------------------------------------- Validities -------------------------------------------------- */
    
    /**
     * Returns whether the given alias is valid.
     * 
     * @param alias the alias to be checked.
     * 
     * @return whether the given alias is valid.
     */
    @Pure
    public static boolean isValidAlias(@Nonnull String alias) {
        return Database.getConfiguration().isValidIdentifier(alias);
    }
    
    /**
     * Returns whether the given prefix is valid.
     * 
     * @param prefix the prefix to be checked.
     * 
     * @return whether the given prefix is valid.
     */
    @Pure
    public final boolean isValidPrefix(@Nonnull String prefix) {
        return prefix.length() + getLengthOfLongestColumnName() <= Database.getConfiguration().getMaximumIdentifierLength() - 1 && Database.getConfiguration().isValidIdentifier(prefix);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Returns this declaration as a string for "create table" statements.
     * 
     * @param nullable whether the columns of this declaration are nullable.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return this declaration as a string for "create table" statements.
     */
    @Pure
    protected abstract @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix);
    
    /**
     * Returns this declaration as a string for "create table" statements.
     * 
     * @return this declaration as a string for "create table" statements.
     */
    @Pure
    @Override
    public final @Nonnull String toString() {
        return toString(false, null);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    /**
     * Stores the name of each (unique) column (prepended with the given alias and prefix) in the given array.
     * 
     * @param unique whether only the names of unique columns shall be returned.
     * @param alias the table alias that is to be prepended to all column names.
     * @param prefix the prefix that is to be prepended to all column names.
     * @param names a mutable array which stores the name of each column.
     * @param index the current index into the names array.
     * 
     * @require names.size() >= index.getValue() + getNumberOfColumns(unique) : "The array has enough space for each column.";
     */
    protected abstract void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index);
    
    /**
     * Returns the name of each (unique) column (prepended with the given alias).
     * 
     * @param unique whether only the names of unique columns shall be returned.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each (unique) column (prepended with the given alias).
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNames(boolean unique, @Nullable @Validated String alias) {
        final @Nonnull FreezableArray<String> names = FreezableArray.get(getNumberOfColumns(unique));
        storeColumnNames(unique, alias, null, names, MutableIndex.get());
        return names;
    }
    
    /**
     * Returns the name of each column prepended with the given alias if not null.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column prepended with the given alias if not null.
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNames(@Nullable @Validated String alias) {
        return getColumnNames(false, alias);
    }
    
    /**
     * Returns the name of each column.
     * 
     * @return the name of each column.
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNames() {
        return getColumnNames(false, null);
    }
    
    /**
     * Returns the name of each primary key column of this declaration.
     * 
     * @return the name of each primary key column of this declaration.
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getPrimaryKeyColumnNames() {
        return getColumnNames(true, null);
    }
    
    /* -------------------------------------------------- Column Types -------------------------------------------------- */
    
    /**
     * Stores the type of each column in the given array.
     * 
     * @param types an array to store the type of each column.
     * @param index the current index into the types array.
     * 
     * @require types.size() >= index.getValue() + getNumberOfColumns() : "The array has enough space for each column.";
     */
    protected abstract void storeColumnTypes(@NonCapturable @Nonnull @NonFrozen FreezableArray<SQLType> types, @Nonnull MutableIndex index);
    
    /**
     * Returns the name of each (unique) column (prepended with the given alias).
     * 
     * @param unique whether only the names of unique columns shall be returned.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each (unique) column (prepended with the given alias).
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<SQLType> getColumnTypes() {
        final @Nonnull FreezableArray<SQLType> types = FreezableArray.get(getNumberOfColumns());
        storeColumnTypes(types, MutableIndex.get());
        return types;
    }
    
    /**
     * Returns whether the column types of this declaration matches the column types of the given declaration.
     * 
     * @param declaration the declaration whose column types are to be compared with those of this declaration.
     * 
     * @return whether the column types of this declaration matches the column types of the given declaration.
     */
    @Pure
    public final boolean matches(@Nonnull Declaration declaration) {
        return this.getColumnTypes().equals(declaration.getColumnTypes());
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    /**
     * Returns (only) the (unique) columns for selection with the given alias.
     * 
     * @param unique whether only the names of unique columns shall be returned.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return (only) the (unique) columns for selection with the given alias.
     */
    @Pure
    private @Nonnull String getSelection(boolean unique, @Nullable @Validated String alias) {
        return IterableConverter.toString(getColumnNames(unique, alias));
    }
    
    /**
     * Returns the columns for selection prepended with the given table alias.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the columns for selection prepended with the given table alias.
     */
    @Pure
    public final @Nonnull String getSelection(@Nullable @Validated String alias) {
        return getSelection(false, alias);
    }
    
    /**
     * Returns the columns of this declaration for selection.
     * 
     * @return the columns of this declaration for selection.
     */
    @Pure
    public final @Nonnull String getSelection() {
        return getSelection(null);
    }
    
    /**
     * Returns the primary key columns of this declaration.
     * 
     * @return the primary key columns of this declaration.
     */
    @Pure
    public final @Nonnull String getPrimaryKeySelection() {
        return getSelection(true, null);
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    /**
     * Returns whether this declaration is {@link Site site}-specific.
     * 
     * @return whether this declaration is {@link Site site}-specific.
     */
    @Pure
    public abstract boolean isSiteSpecific();
    
    /**
     * Returns the foreign key constraints of this declaration.
     * 
     * @param site the site at which this declaration is used.
     * @param prefix the prefix prepended to all column names.
     * 
     * @return the foreign key constraints of this declaration.
     * 
     * @require !isSiteSpecific() || site != null : "If this declaration is site-specific, the site may not be null.";
     * 
     * @ensure return.isEmpty() || return.startsWith(",") : "The returned string is either empty or starts with a comma.";
     */
    @Locked
    @NonCommitting
    protected abstract @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws SQLException;
    
    /**
     * Returns the foreign key constraints of this declaration.
     * 
     * @param site the site at which this declaration is used.
     * 
     * @return the foreign key constraints of this declaration.
     * 
     * @require !isSiteSpecific() || site != null : "If this declaration is site-specific, the site may not be null.";
     * 
     * @ensure return.isEmpty() || return.startsWith(",") : "The returned string is either empty or starts with a comma.";
     */
    @Locked
    @NonCommitting
    public final @Nonnull String getForeignKeys(@Nullable Site site) throws SQLException {
        return getForeignKeys(site, null);
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    /**
     * This method is called after the creation of a table on its declaration.
     * (The identity declaration overrides this method to create indexes on identity columns and register them at the mapper.)
     * 
     * @param statement the statement which was used.
     * @param table the table which has been created.
     * @param site the site at which this declaration is used.
     * @param unique whether this declaration is considered to be unique.
     * @param prefix the prefix that is to be prepended to the column names of this declaration.
     * 
     * @require !table.isSiteSpecific() || site != null : "If the table is site-specific, the site may not be null.";
     */
    @Locked
    @NonCommitting
    public abstract void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws SQLException;
    
    /**
     * This method is called before the deletion of a table on its declaration.
     * (The identity converter overrides this method to delete indexes on identity columns and deregister them at the mapper.)
     * 
     * @param statement the statement which was used.
     * @param table the table which has been deleted.
     * @param site the site at which this declaration is used.
     * @param unique whether this declaration is considered to be unique.
     * @param prefix the prefix that is to be prepended to the column names of this declaration.
     * 
     * @require !table.isSiteSpecific() || site != null : "If the table is site-specific, the site may not be null.";
     */
    @Locked
    @NonCommitting
    public abstract void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws SQLException;
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    // Most of this section will hopefully soon be replaced by the SQL builder.
    
    /**
     * Returns as many question marks as columns in this declaration separated by commas.
     * 
     * @return as many question marks as columns in this declaration separated by commas.
     */
    @Pure
    public final @Nonnull String getInsertionForPreparedStatement() {
        return IterableConverter.toString(FreezableArray.<String>get(getNumberOfColumns()).setAll("?"));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark.
     * 
     * @ensure return.size() == getNumberOfColumns() : "The returned array contains an entry for each column.";
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnNamesEqualQuestionMarks(@Nullable @Validated String alias) {
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> names = getColumnNames(alias);
        for (int i = 0; i < getNumberOfColumns(); i++) {
            names.set(i, names.getNonNullable(i) + " = ?");
        }
        return names;
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by commas.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForPreparedStatement() {
        return IterableConverter.toString(getColumnNamesEqualQuestionMarks(null));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     * 
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForPreparedStatement(@Nullable @Validated String alias) {
        return IterableConverter.toString(getColumnNamesEqualQuestionMarks(alias), " AND ");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     * 
     * @return the name of each column followed by the equality sign and a question mark separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForPreparedStatement() {
        return getConditionForPreparedStatement(null);
    }
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to null.
     * The number of parameters that are set is given by {@link #getNumberOfColumns()}.
     * 
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public abstract void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull MutableIndex parameterIndex) throws SQLException;
    
    /* -------------------------------------------------- Extensions -------------------------------------------------- */
    
    /**
     * Returns a nullable version of this declaration.
     * 
     * @return a nullable version of this declaration.
     */
    @Pure
    public final @Nonnull NullableDeclaration nullable() {
        return NullableDeclaration.get(this);
    }
    
    /**
     * Returns an indexed version of this declaration.
     * 
     * @return an indexed version of this declaration.
     */
    @Pure
    public final @Nonnull IndexingDeclaration indexed() {
        return IndexingDeclaration.get(this);
    }
    
    /**
     * Returns a non-unique version of this declaration.
     * 
     * @return a non-unique version of this declaration.
     */
    @Pure
    public final @Nonnull NonUniqueDeclaration nonUnique() {
        return NonUniqueDeclaration.get(this);
    }
    
    /**
     * Returns this declaration prefixed with the given string.
     * 
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return this declaration prefixed with the given string.
     */
    @Pure
    public final @Nonnull PrefixingDeclaration prefixedWith(@Nonnull @Validated String prefix) {
        return PrefixingDeclaration.get(this, prefix);
    }
    
    /**
     * Returns this declaration combined with the given declaration.
     * 
     * @return this declaration combined with the given declaration.
     */
    @Pure
    public final @Nonnull CombiningDeclaration combinedWith(@Nonnull Declaration declaration) {
        return CombiningDeclaration.get(this, declaration);
    }
    
}
