package net.digitalid.database.dialect.statement.select.unordered.simple.columns;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.table.SQLTable;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node selects all columns of the given table.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLAllColumns extends SQLResultColumnOrAllColumns {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * The table whose columns are to be selected or null to select all columns.
     */
    @Pure
    public @Nullable SQLTable getTable();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        final @Nullable SQLTable table = getTable();
        if (table != null) {
            dialect.unparse(table, site, string);
            string.append(".");
        }
        string.append("*");
    }
    
}
