package net.digitalid.database.core.declaration;

import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Validated;
import net.digitalid.utility.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;

/**
 * This class delegates all methods to another declaration.
 * 
 * @see NullableDeclaration
 * @see IndexingDeclaration
 * @see NonUniqueDeclaration
 * @see PrefixingDeclaration
 */
@Immutable
public abstract class ChainingDeclaration extends Declaration {
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Stores the declaration on which this declaration is based.
     */
    private final @Nonnull Declaration declaration;
    
    /**
     * Returns the declaration on which this declaration is based.
     * 
     * @return the declaration on which this declaration is based.
     */
    @Pure
    public final @Nonnull Declaration getDeclaration() {
        return declaration;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new chaining declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     */
    protected ChainingDeclaration(@Nonnull Declaration declaration) {
        this.declaration = declaration;
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    protected int getNumberOfColumns(boolean unique) {
        return declaration.getNumberOfColumns(unique);
    }
    
    @Pure
    @Override
    public int getLengthOfLongestColumnName() {
        return declaration.getLengthOfLongestColumnName();
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix) {
        return declaration.toString(nullable, prefix);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        declaration.storeColumnNames(unique, alias, prefix, names, index);
    }
    
    /* -------------------------------------------------- Column Types -------------------------------------------------- */
    
    @Override
    protected final void storeColumnTypes(@NonCapturable @Nonnull @NonFrozen FreezableArray<SQLType> types, @Nonnull MutableIndex index) {
        declaration.storeColumnTypes(types, index);
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Pure
    @Override
    public final boolean isSiteSpecific() {
        return declaration.isSiteSpecific();
    }
    
    @Locked
    @Override
    @NonCommitting
    protected @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws FailedOperationException {
        return declaration.getForeignKeys(site, prefix);
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        declaration.executeAfterCreation(statement, table, site, unique, prefix);
    }
    
    @Locked
    @Override
    @NonCommitting
    public void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        declaration.executeBeforeDeletion(statement, table, site, unique, prefix);
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public final void storeNull(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        declaration.storeNull(preparedStatement, parameterIndex);
    }
    
}