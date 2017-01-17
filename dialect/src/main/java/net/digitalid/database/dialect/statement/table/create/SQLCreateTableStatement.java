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
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL create table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLCreateTableStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    /**
     * Returns the column declarations.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<SQLColumnDeclaration> getColumnDeclarations();
    
    /* -------------------------------------------------- Table Constraints -------------------------------------------------- */
    
    /**
     * Returns the optional table constraints.
     */
    @Pure
    public @Nullable @NonNullableElements @NonEmpty ImmutableList<SQLTableConstraint> getTableConstraints();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("CREATE TABLE IF NOT EXISTS ");
        dialect.unparse(getTable(), site, string);
        string.append("(");
        dialect.unparse(getColumnDeclarations(), site, string);
        final @Nullable @NonNullableElements @NonEmpty ImmutableList<SQLTableConstraint> tableConstraints = getTableConstraints();
        if (tableConstraints != null) {
            string.append(", ");
            dialect.unparse(tableConstraints, site, string);
        }
        string.append(")");
    }
    
}
