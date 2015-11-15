package net.digitalid.utility.database.declaration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.reference.Reference;
import net.digitalid.utility.database.site.Site;
import net.digitalid.utility.database.table.Table;

/**
 * This class implements the declaration of a single column.
 */
@Immutable
public class ColumnDeclaration extends Declaration {
    
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
     * Stores the name of this column declaration.
     */
    private final @Nonnull @Validated String name;
    
    /**
     * Returns the name of this column declaration.
     * 
     * @return the name of this column declaration.
     */
    @Pure
    public final @Nonnull @Validated String getName() {
        return name;
    }
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    /**
     * Stores the SQL type of this column declaration.
     */
    private final @Nonnull SQLType type;
    
    /**
     * Returns the SQL type of this column declaration.
     * 
     * @return the SQL type of this column declaration.
     */
    @Pure
    public final @Nonnull SQLType getType() {
        return type;
    }
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
    
    /**
     * Stores the foreign key reference of this column declaration or null if there is none.
     */
    private final @Nullable Reference reference;
    
    /**
     * Returns the foreign key reference of this column declaration or null if there is none.
     * 
     * @return the foreign key reference of this column declaration or null if there is none.
     */
    @Pure
    public final @Nullable Reference getReference() {
        return reference;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column declaration with the given parameters.
     * 
     * @param name the name of the new column declaration.
     * @param type the SQL type of the new column declaration.
     * @param reference the foreign key reference of the new column declaration or null if there is none.
     */
    protected ColumnDeclaration(@Nonnull @Validated String name, @Nonnull SQLType type, @Nullable Reference reference) {
        assert isValidName(name) : "The name is valid.";
        
        this.name = name;
        this.type = type;
        this.reference = reference;
    }
    
    /**
     * Returns a new column declaration with the given parameters.
     * 
     * @param name the name of the new column declaration.
     * @param type the SQL type of the new column declaration.
     * @param reference the foreign key reference of the new column declaration or null if there is none.
     * 
     * @return a new column declaration with the given parameters.
     */
    @Pure
    public static @Nonnull ColumnDeclaration get(@Nonnull @Validated String name, @Nonnull SQLType type, @Nullable Reference reference) {
        return new ColumnDeclaration(name, type, reference);
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    protected final int getNumberOfColumns(boolean unique) {
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
    protected @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix) {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
        
        return (prefix == null ? "" : prefix + "_") + name + " " + type + (nullable ? "" : " NOT NULL");
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected final void getColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        assert alias == null || isValidAlias(alias) : "The alias is null or valid.";
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
        
        names.set(index.getAndIncrementValue(), (alias == null ? "" : alias + ".") + (prefix == null ? "" : prefix + "_") + name);
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Pure
    @Override
    public final boolean isSiteSpecific() {
        return reference == null ? false : reference.getTable().isSiteSpecific();
    }
    
    @Locked
    @Override
    @NonCommitting
    protected final @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws SQLException {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
        
        if (reference != null) { return ", FOREIGN KEY (" + (reference.isEntityDependent() ? "entity, " : "") + (prefix == null ? "" : prefix + "_") + name + ") " + reference.get(site); }
        else { return ""; }
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws SQLException {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
    }
    
    @Locked
    @Override
    @NonCommitting
    public void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws SQLException {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull MutableIndex parameterIndex) throws SQLException {
        preparedStatement.setNull(parameterIndex.getAndIncrementValue(), getType().getCode());
    }
    
}
