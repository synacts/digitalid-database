package net.digitalid.utility.database.converter;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.Capturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.column.Reference;
import net.digitalid.utility.database.column.SQLType;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.site.Site;

/**
 * A column SQL converter allows to store and restore objects into and from a single column of the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 */
@Immutable
public abstract class ColumnSQLConverter<O, E> extends AbstractSQLConverter<O, E> {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns whether the given columnName is valid.
     * 
     * @param name the columnName to be checked.
     * 
     * @return whether the given columnName is valid.
     */
    @Pure
    public static boolean isValidName(@Nonnull String name) {
        return name.length() <= 22 && !name.equalsIgnoreCase("entity") && Database.getConfiguration().isValidIdentifier(name); // TODO: Why to exclude "entity" and limit the length? Remove both!
    }
    
    /**
     * Stores the columnName of this column.
     */
    private final @Nonnull @Validated String columnName;
    
    /**
     * Returns the columnName of this column.
     * 
     * @return the columnName of this column.
     */
    @Pure
    public final @Nonnull @Validated String getName() {
        return columnName;
    }
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    /**
     * Stores the SQL type of this column.
     */
    private final @Nonnull SQLType type;
    
    /**
     * Returns the SQL type of this column.
     * 
     * @return the SQL type of this column.
     */
    @Pure
    public final @Nonnull SQLType getType() {
        return type;
    }
    
    /* -------------------------------------------------- Nullable -------------------------------------------------- */
    
    /**
     * Stores whether this column is nullable.
     */
    private final boolean nullable; // TODO: I think this should not be determined by the column but rather by its occurrence (the table). Or both?
    
    /**
     * Returns whether this column is nullable.
     * 
     * @return whether this column is nullable.
     */
    @Pure
    public final boolean isNullable() {
        return nullable;
    }
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
    
    /**
     * Stores the foreign key reference of this column or null if there is none.
     */
    private final @Nullable Reference reference;
    
    /**
     * Returns the foreign key reference of this column or null if there is none.
     * 
     * @return the foreign key reference of this column or null if there is none.
     */
    @Pure
    public final @Nullable Reference getReference() {
        return reference;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column SQL converter with the given parameters.
     * 
     * @param name the columnName of the new column.
     * @param type the SQL type of the new column.
     * @param nullable whether the new column is nullable.
     * @param reference the foreign key reference of the new column or null if there is none.
     */
    protected ColumnSQLConverter(@Nonnull @Validated String name, @Nonnull SQLType type, boolean nullable, @Nullable Reference reference) {
        assert isValidName(name) : "The name is valid.";
        
        this.columnName = name;
        this.type = type;
        this.nullable = nullable;
        this.reference = reference;
    }
    
    /**
     * Returns a new column SQL converter with the given parameters.
     * 
     * @param name the columnName of the new column.
     * @param type the SQL type of the new column.
     * @param nullable whether the new column is nullable.
     * @param reference the foreign key reference of the new column or null if there is none.
     * 
     * @return a new column SQL converter with the given parameters.
     */
    public static final @Nonnull <O, E> ColumnSQLConverter<O, E> get(@Nonnull @Validated String name, @Nonnull SQLType type, boolean nullable, @Nullable Reference reference) {
        return new ColumnSQLConverter<>(name, type, nullable, reference);
    }
    
    /**
     * Returns a new column SQL converter with the given parameters for a non-nullable value.
     * 
     * @param name the columnName of the new column.
     * @param type the SQL type of the new column.
     * 
     * @return a new column SQL converter with the given parameters for a non-nullable value.
     */
    public static final @Nonnull <O, E> ColumnSQLConverter<O, E> get(@Nonnull @Validated String name, @Nonnull SQLType type) {
        return get(name, type, false, null);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Returns the abstract SQL converters as declaration with the given prefix.
     * 
     * @param prefix the prefix to prepend to all column names.
     * 
     * @return the column as declaration with the given prefix.
     */
    @Pure
    @Override
    public final @Nonnull String getDeclaration(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return (prefix.isEmpty() ? "" : prefix + "_") + columnName + " " + type + (nullable ? "" : " NOT NULL");
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getSelection(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return (prefix.isEmpty() ? "" : prefix + "_") + columnName;
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public @Nonnull String getForeignKeys(@Nonnull @Validated String prefix, @Nonnull Site site) throws SQLException {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        if (reference != null) return ", FOREIGN KEY (" + (reference.isEntityDependent() ? "entity, " : "") + (prefix.isEmpty() ? "" : prefix + "_") + columnName + ") " + reference.get(site);
        else return "";
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    @Pure
    @Override
    protected final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnsEqualValues(@Nonnull @Validated String alias, @Nonnull @Validated String prefix, @Nullable O object) {
        assert isValidAlias(alias) : "The alias is valid.";
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return FreezableArray.getNonNullable((alias.isEmpty() ? "" : alias + ".") + prefix + columnName + " = " + getValuesOrNulls(object));        
    }

    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    public final int getNumberOfColumns() {
        return 1;
    }
    
    @Pure
    @Override
    public final int getMaximumColumnNameLength() {
        return columnName.length();
    }
    
}
