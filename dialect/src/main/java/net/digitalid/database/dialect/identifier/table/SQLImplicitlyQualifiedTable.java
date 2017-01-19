package net.digitalid.database.dialect.identifier.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL implicitly qualified table.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLImplicitlyQualifiedTable extends SQLQualifiedTable {
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(SQLSchemaNameBuilder.withString(site.getSchemaName()).build(), site, string);
        string.append(".");
        dialect.unparse(getTable(), site, string);
    }
    
}
