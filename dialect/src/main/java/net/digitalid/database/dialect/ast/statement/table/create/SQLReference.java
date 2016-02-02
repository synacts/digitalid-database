package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.method.Pure;

import net.digitalid.database.dialect.table.Table;

/**
 * This class models single-column foreign key references.
 */
@Immutable
public class SQLReference {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Stores the database table whose column is referenced.
     */
    private final @Nonnull Table table;
    
    /**
     * Returns the database table whose column is referenced.
     * 
     * @return the database table whose column is referenced.
     */
    @Pure
    public final @Nonnull Table getTable() {
        return table;
    }
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
//    /**
//     * Stores the referenced column within the specified table.
//     */
//    private final @Nonnull ColumnDeclaration column;
//    
//    /**
//     * Returns the referenced column within the specified table.
//     * 
//     * @return the referenced column within the specified table.
//     */
//    @Pure
//    public final @Nonnull ColumnDeclaration getColumn() {
//        return column;
//    }
//    
    /* -------------------------------------------------- Delete Option -------------------------------------------------- */
    
    /**
     * Stores the referential action triggered on deletion.
     */
    private final @Nonnull SQLReferenceOption deleteOption;
    
    /**
     * Returns the referential action triggered on deletion.
     * 
     * @return the referential action triggered on deletion.
     */
    @Pure
    public final @Nonnull SQLReferenceOption getDeleteOption() {
        return deleteOption;
    }
    
    /* -------------------------------------------------- Update Option -------------------------------------------------- */
    
    /**
     * Stores the referential action triggered on update.
     */
    private final @Nonnull SQLReferenceOption updateOption;
    
    /**
     * Returns the referential action triggered on update.
     * 
     * @return the referential action triggered on update.
     */
    @Pure
    public final @Nonnull SQLReferenceOption getUpdateOption() {
        return updateOption;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new column reference with the given parameters.
     * 
     * @param table the database table whose column is referenced.
     * @param deleteOption the referential action triggered on deletion.
     * @param updateOption the referential action triggered on update.
     */
//    protected SQLReference(@Nonnull Table table, @Nonnull ColumnDeclaration column, @Nonnull SQLReferenceOption deleteOption, @Nonnull SQLReferenceOption updateOption) {
    protected SQLReference(@Nonnull Table table, @Nonnull SQLReferenceOption deleteOption, @Nonnull SQLReferenceOption updateOption) {
        this.table = table;
        this.deleteOption = deleteOption;
        this.updateOption = updateOption;
    }
    
    /* -------------------------------------------------- Retrieval -------------------------------------------------- */
    
    /**
     * Returns the reference to the table of the given site after creating it first.
     * 
     * @param site the site at which the foreign key constraint is declared and used.
     * 
     * @return the reference to the table of the given site after creating it first.
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    // TODO: replace with transcribe.
//    @Locked
//    @NonCommitting
//    public @Nonnull String get(@Nonnull Site site) throws FailedOperationException {
//        table.create(site);
//        return "REFERENCES " + table.getName(site) + " (entity, " + getColumn().getName() + ") ON DELETE " + getDeleteOption() + " ON UPDATE " + getUpdateOption();
//    }
    
}
