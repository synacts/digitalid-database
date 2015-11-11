package net.digitalid.utility.database.converter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.column.ColumnIndex;
import net.digitalid.utility.database.column.SQLType;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.reference.ColumnReference;
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
     * Returns whether the given name is valid.
     * 
     * @param name the name to be checked.
     * 
     * @return whether the given name is valid.
     */
    @Pure
    public static boolean isValidName(@Nonnull String name) {
        return Database.getConfiguration().isValidIdentifier(name);
    }
    
    /**
     * Stores the name of this column.
     */
    private final @Nonnull @Validated String name;
    
    /**
     * Returns the name of this column.
     * 
     * @return the name of this column.
     */
    @Pure
    public final @Nonnull @Validated String getName() {
        return name;
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
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
    
    /**
     * Stores the foreign key reference of this column or null if there is none.
     */
    private final @Nullable ColumnReference reference;
    
    /**
     * Returns the foreign key reference of this column or null if there is none.
     * 
     * @return the foreign key reference of this column or null if there is none.
     */
    @Pure
    public final @Nullable ColumnReference getReference() {
        return reference;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column SQL converter with the given parameters.
     * 
     * @param name the name of the new column.
     * @param type the SQL type of the new column.
     * @param reference the foreign key reference of the new column or null if there is none.
     */
    protected ColumnSQLConverter(@Nonnull @Validated String name, @Nonnull SQLType type, @Nullable ColumnReference reference) {
        assert isValidName(name) : "The name is valid.";
        
        this.name = name;
        this.type = type;
        this.reference = reference;
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    public final int getNumberOfColumns() {
        return 1;
    }
    
    @Pure
    @Override
    public final int getLengthOfLongestColumnName() {
        return name.length();
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getDeclaration(boolean nullable, @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return (prefix.isEmpty() ? "" : prefix + "_") + name + " " + type + (nullable ? "" : " NOT NULL");
    }
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    @Pure
    @Override
    public final @Nonnull String getSelection(final @Nonnull @Validated String prefix) {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        return (prefix.isEmpty() ? "" : prefix + "_") + name;
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public final @Nonnull String getForeignKeys(@Nonnull Site site, @Nonnull @Validated String prefix) throws SQLException {
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        if (reference != null) { return ", FOREIGN KEY (" + (reference.isEntityDependent() ? "entity, " : "") + (prefix.isEmpty() ? "" : prefix + "_") + name + ") " + reference.get(site); }
        else { return ""; }
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    /**
     * Returns the value of the given object for this column.
     * 
     * @param object the object whose value is returned.
     *  
     * @return the value of the given object for this column.
     */
    @Pure
    protected abstract @Nonnull String getValue(@Nonnull O object);
    
    @Override
    public final void getValues(@Nonnull O object, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> values, @Nonnull ColumnIndex index) {
        values.set(index.getAndIncrementValue(), getValue(object));
    }
    
    @Override
    protected final void getColumnNames(@Nonnull @Validated String alias, @Nonnull @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull ColumnIndex index) {
        assert isValidAlias(alias) : "The alias is valid.";
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        names.set(index.getAndIncrementValue(), (alias.isEmpty() ? "" : alias + ".") + (prefix.isEmpty() ? "" : prefix + "_") + name);
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull ColumnIndex parameterIndex) throws SQLException {
        preparedStatement.setNull(parameterIndex.getAndIncrementValue(), getType().getCode());
    }
    
}
