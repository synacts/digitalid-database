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
import net.digitalid.utility.collections.index.MutableIndex;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;

/**
 * This class implements a declaration that combines several declarations.
 */
@Immutable
public final class CombiningDeclaration extends Declaration {
    
    /* -------------------------------------------------- Declarations -------------------------------------------------- */
    
    /**
     * Stores the declarations on which this declaration is based.
     */
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<Declaration> declarations;
    
    /**
     * Returns the declarations on which this declaration is based.
     * 
     * @return the declarations on which this declaration is based.
     */
    @Pure
    public final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<Declaration> getDeclarations() {
        return declarations;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new combining declaration with the given declarations.
     * 
     * @param declarations the declarations on which the new declaration is based.
     */
    private CombiningDeclaration(@Nonnull Declaration... declarations) {
        this.declarations = FreezableArray.getNonNullable(declarations).freeze();
    }
    
    /**
     * Returns a new combining declaration with the given declarations.
     * 
     * @param declarations the declarations on which the new declaration is based.
     * 
     * @return a new combining declaration with the given declarations.
     */
    @Pure
    public static @Nonnull CombiningDeclaration get(@Nonnull Declaration... declarations) {
        return new CombiningDeclaration(declarations);
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    protected int getNumberOfColumns(boolean unique) {
        int numberOfColumns = 0;
        for (final @Nonnull Declaration declaration : declarations) {
            numberOfColumns += declaration.getNumberOfColumns(unique);
        }
        return numberOfColumns;
    }
    
    @Pure
    @Override
    public int getLengthOfLongestColumnName() {
        int lengthOfLongestColumnName = 0;
        for (final @Nonnull Declaration declaration : declarations) {
            final int columnNameLength = declaration.getLengthOfLongestColumnName();
            if (columnNameLength > lengthOfLongestColumnName) { lengthOfLongestColumnName = columnNameLength; }
        }
        return lengthOfLongestColumnName;
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String toString(final boolean nullable, final @Nullable @Validated String prefix) {
        return IterableConverter.toString(declarations, new ElementConverter<Declaration>() { 
            @Pure @Override public String toString(@Nullable Declaration declaration) { 
                assert declaration != null : "The declaration is not null.";
                
                return declaration.toString(nullable, prefix);
            }
        });
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        for (final @Nonnull Declaration declaration : declarations) {
            declaration.storeColumnNames(unique, alias, prefix, names, index);
        }
    }
    
    /* -------------------------------------------------- Column Types -------------------------------------------------- */
    
    @Override
    protected final void storeColumnTypes(@NonCapturable @Nonnull @NonFrozen FreezableArray<SQLType> types, @Nonnull MutableIndex index) {
        for (final @Nonnull Declaration declaration : declarations) {
            declaration.storeColumnTypes(types, index);
        }
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isSiteSpecific() {
        for (final @Nonnull Declaration declaration : declarations) {
            if (declaration.isSiteSpecific()) { return true; }
        }
        return false;
    }
    
    @Locked
    @Override
    @NonCommitting
    protected @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws FailedOperationException {
        // Cannot use IterableConverter.toString() here because the getForeignKeys() method can throw an SQLException.
        final @Nonnull StringBuilder string = new StringBuilder();
        for (final @Nonnull Declaration declaration : declarations) {
            string.append(declaration.getForeignKeys(site, prefix));
        }
        return string.toString();
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        for (final @Nonnull Declaration declaration : declarations) {
            declaration.executeAfterCreation(statement, table, site, unique, prefix);
        }
    }
    
    @Locked
    @Override
    @NonCommitting
    public void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        for (final @Nonnull Declaration declaration : declarations) {
            declaration.executeBeforeDeletion(statement, table, site, unique, prefix);
        }
    }
    
    /* -------------------------------------------------- Storing (with PreparedStatement) -------------------------------------------------- */
    
    @Override
    @NonCommitting
    public void storeNull(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        for (final @Nonnull Declaration declaration : declarations) {
            declaration.storeNull(preparedStatement, parameterIndex);
        }
    }
    
}
