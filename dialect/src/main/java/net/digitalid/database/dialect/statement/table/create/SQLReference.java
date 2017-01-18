package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.subject.site.Site;

/**
 * This type models a foreign key reference.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLReference extends SQLNode {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table whose columns are referenced.
     */
    @Pure
    public @Nonnull SQLQualifiedTable getTable();
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the referenced columns within the given table.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> getColumns();
    
    /* -------------------------------------------------- Delete Option -------------------------------------------------- */
    
    /**
     * Returns the referential action triggered on deletion.
     */
    @Pure
    public @Nullable SQLReferenceOption getDeleteOption();
    
    /* -------------------------------------------------- Update Option -------------------------------------------------- */
    
    /**
     * Returns the referential action triggered on update.
     */
    @Pure
    public @Nullable SQLReferenceOption getUpdateOption();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(" REFERENCES ");
        dialect.unparse(getTable(), site, string);
        string.append(" (");
        dialect.unparse(getColumns(), site, string);
        string.append(")");
        
        final @Nullable SQLReferenceOption deleteOption = getDeleteOption();
        if (deleteOption != null) {
            string.append(" ON DELETE ");
            dialect.unparse(deleteOption, site, string);
        }
        
        final @Nullable SQLReferenceOption updateOption = getUpdateOption();
        if (updateOption != null) {
            string.append(" ON UPDATE ");
            dialect.unparse(updateOption, site, string);
        }
    }
    
}
