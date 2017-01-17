package net.digitalid.database.dialect.statement.delete;

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
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL delete statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLDeleteStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Where Clause -------------------------------------------------- */
    
    /**
     * Returns the optional where clause of this delete statement.
     */
    @Pure
    public @Nullable SQLBooleanExpression getWhereClause();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("DELETE FROM ");
        dialect.unparse(getTable(), site, string);
        final @Nullable SQLBooleanExpression whereClause = getWhereClause();
        if (whereClause != null) {
            string.append(" WHERE ");
            dialect.unparse(whereClause, site, string);
        }
    }
    
}
