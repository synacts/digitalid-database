package net.digitalid.database.core.declaration;

import java.sql.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Validated;

import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.core.table.Table;

/**
 * This class implements an indexing version of another declaration.
 */
@Immutable
public final class IndexingDeclaration extends ChainingDeclaration {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new indexing declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     */
    private IndexingDeclaration(@Nonnull Declaration declaration) {
        super(declaration);
    }
    
    /**
     * Returns a new indexing declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     * 
     * @return a new indexing declaration with the given declaration.
     */
    @Pure
    public static @Nonnull IndexingDeclaration get(@Nonnull Declaration declaration) {
        return new IndexingDeclaration(declaration);
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    protected @Nonnull String getForeignKeys(@Nullable Site site, @Nullable @Validated String prefix) throws FailedOperationException {
        return super.getForeignKeys(site, prefix) + ", " + Database.getConfiguration().INDEX(getColumnNames().toArray());
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    @Locked
    @Override
    @NonCommitting
    public void executeAfterCreation(@Nonnull Statement statement, @Nonnull Table table, @Nullable Site site, boolean unique, @Nullable @Validated String prefix) throws FailedUpdateExecutionException {
        super.executeAfterCreation(statement, table, site, unique, prefix);
        Database.getConfiguration().createIndex(statement, table.getName(site), getColumnNames().toArray());
    }
    
}
