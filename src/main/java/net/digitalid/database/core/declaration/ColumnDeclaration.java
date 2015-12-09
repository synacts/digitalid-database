package net.digitalid.database.core.declaration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.sql.statement.table.create.SQLReference;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;

/**
 * This class implements the declaration of a single column.
 * 
 * @see AutoIncrementingColumnDeclaration
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
    
    /**
     * Returns the name of this column declaration with the given prefix.
     * 
     * @param prefix the prefix that is to be prepended to this declaration.
     * 
     * @return the name of this column declaration with the given prefix.
     */
    @Pure
    public final @Nonnull @Validated String getName(@Nullable @Validated String prefix) {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
        
        return (prefix == null ? "" : prefix + "_") + name;
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
    private final @Nullable SQLReference reference;
    
    /**
     * Returns the foreign key reference of this column declaration or null if there is none.
     * 
     * @return the foreign key reference of this column declaration or null if there is none.
     */
    @Pure
    public final @Nullable SQLReference getReference() {
        return reference;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column declaration with the given parameters.
     * 
     * @param name the name of the new column declaration.
     * @param type the SQL type of the new column declaration.
     * @param reference the foreign key reference of the new column declaration or null if there is none.
     * 
     * @require reference == null || reference.getColumn().getType() == type : "If the reference is not null, the type of its column is the same as the given type.";
     */
    protected ColumnDeclaration(@Nonnull @Validated String name, @Nonnull SQLType type, @Nullable SQLReference reference) {
        assert isValidName(name) : "The name is valid.";
        assert reference == null || reference.getColumn().getType() == type : "If the reference is not null, the type of its column is the same as the given type.";
        
        this.name = name;
        this.type = type;
        this.reference = reference;
    }
    
    /**
     * Returns a new column declaration with the given parameters.
     * 
     * @param name the name of the new column declaration.
     * @param type the SQL type of the new column declaration.
     * 
     * @return a new column declaration with the given parameters.
     */
    @Pure
    public static @Nonnull ColumnDeclaration get(@Nonnull @Validated String name, @Nonnull SQLType type) {
        return new ColumnDeclaration(name, type, null);
    }
    
    /**
     * Returns a new column declaration with the given parameters.
     * 
     * @param name the name of the new column declaration.
     * @param reference the foreign key reference of the new column declaration.
     * 
     * @return a new column declaration with the given parameters.
     */
    @Pure
    public static @Nonnull ColumnDeclaration get(@Nonnull @Validated String name, @Nonnull SQLReference reference) {
        return new ColumnDeclaration(name, reference.getColumn().getType(), reference);
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
        return getName(prefix) + " " + type + (nullable ? "" : " NOT NULL");
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected final void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        assert alias == null || isValidAlias(alias) : "The alias is null or valid.";
        
        names.set(index.getAndIncrementValue(), (alias == null ? "" : alias + ".") + getName(prefix));
    }
    
    /* -------------------------------------------------- Column Types -------------------------------------------------- */
    
    @Override
    protected final void storeColumnTypes(@NonCapturable @Nonnull @NonFrozen FreezableArray<SQLType> types, @Nonnull MutableIndex index) {
        types.set(index.getAndIncrementValue(), type);
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isSiteSpecific() {
        return reference == null ? false : reference.isSiteSpecific();
    }
    
    @Locked
    @Override
    @NonCommitting
    protected @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws FailedOperationException {
        if (reference != null) { return ", FOREIGN KEY (" + (reference.isSiteSpecific() ? "entity, " : "") + getName(prefix) + ") " + reference.get(site); }
        else { return ""; }
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
    }
    
    @Locked
    @Override
    @NonCommitting
    public void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNull(@Nonnull PreparedStatement preparedStatement, @Nonnull MutableIndex parameterIndex) throws FailedValueStoringException {
        try {
            preparedStatement.setNull(parameterIndex.getAndIncrementValue(), getType().getCode());
        } catch (@Nonnull SQLException exception) {
            throw FailedValueStoringException.get(exception);
        }
    }
    
    /* -------------------------------------------------- Renaming -------------------------------------------------- */
    
    /**
     * Returns a new, renamed column declaration with the same column type and reference.
     * 
     * @param name the name of the returned column declaration with the same other fields.
     * 
     * @return a new, renamed column declaration with the same column type and reference.
     */
    @Pure
    public final @Nonnull ColumnDeclaration renamedAs(@Nonnull @Validated String name) {
        return new ColumnDeclaration(name, type, reference);
    }
    
}
