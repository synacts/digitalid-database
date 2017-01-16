package net.digitalid.database.dialect.statement.table.drop;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLStatement;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL drop table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLDropTableStatement extends SQLStatement {
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("DROP TABLE IF EXISTS ");
        dialect.unparse(getTable(), site, string);
    }
    
}
