package net.digitalid.database.core.declaration;

import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.noncommitting.FailedUpdateExecutionException;
import net.digitalid.database.core.site.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;

/**
 * This class implements a prefixing version of another declaration.
 */
@Immutable
public final class PrefixingDeclaration extends ChainingDeclaration {
    
    /* -------------------------------------------------- Prefix -------------------------------------------------- */
    
    /**
     * Stores the prefix that is prepended to all column names.
     */
    private final @Nonnull @Validated String prefix;
    
    /**
     * Returns the prefix that is prepended to all column names.
     * 
     * @return the prefix that is prepended to all column names.
     */
    @Pure
    public final @Nonnull @Validated String getPrefix() {
        return prefix;
    }
    
    /**
     * Returns the prefix that is prepended to all column names.
     * 
     * @param prefix the prefix that is already prepended above.
     * 
     * @return the prefix that is prepended to all column names.
     */
    @Pure
    private @Nonnull @Validated String getPrefix(@Nullable @Validated String prefix) {
        return (prefix == null ? "" : prefix + "_") + this.prefix;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new prefixing declaration with the given declaration and prefix.
     * 
     * @param declaration declaration on which the new declaration is based.
     * @param prefix the prefix that is to be prepended to all column names.
     */
    private PrefixingDeclaration(@Nonnull Declaration declaration, @Nonnull @Validated String prefix) {
        super(declaration);
        
        assert isValidPrefix(prefix) : "The prefix is valid.";
        
        this.prefix = prefix;
    }
    
    /**
     * Returns a new prefixing declaration with the given declaration and prefix.
     * 
     * @param declaration declaration on which the new declaration is based.
     * @param prefix the prefix that is to be prepended to all column names.
     * 
     * @return a new prefixing declaration with the given declaration and prefix.
     */
    @Pure
    public static @Nonnull PrefixingDeclaration get(@Nonnull Declaration declaration, @Nonnull @Validated String prefix) {
        return new PrefixingDeclaration(declaration, prefix);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix) {
        return getDeclaration().toString(nullable, getPrefix(prefix));
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        getDeclaration().storeColumnNames(unique, alias, getPrefix(prefix), names, index);
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        getDeclaration().executeAfterCreation(statement, table, site, unique, getPrefix(prefix));
    }
    
    @Locked
    @Override
    @NonCommitting
    public void executeBeforeDeletion(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        getDeclaration().executeBeforeDeletion(statement, table, site, unique, getPrefix(prefix));
    }
    
}
