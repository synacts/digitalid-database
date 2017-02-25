package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.unit.Unit;

import static net.digitalid.utility.conversion.model.CustomType.*;

/**
 * An SQL type.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLType extends SQLNode {
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    /**
     * Returns the underlying type.
     */
    @Pure
    public @Nonnull CustomType getType();
    
    /* -------------------------------------------------- Translation -------------------------------------------------- */
    
    /**
     * Returns this type in SQL.
     */
    @Pure
    public default @Nonnull String getTypeInSQL() {
        final @Nonnull CustomType type = getType();
        if (type == BOOLEAN) { return "BOOLEAN"; }
        if (type == INTEGER08) { return "TINYINT"; }
        if (type == INTEGER16) { return "SMALLINT"; }
        if (type == INTEGER32) { return "INT"; }
        if (type == INTEGER64) { return "BIGINT"; }
        if (type == INTEGER) { return "BLOB"; }
        if (type == DECIMAL32) { return "FLOAT"; }
        if (type == DECIMAL64) { return "DOUBLE"; }
        if (type == STRING01) { return "CHAR(1)"; }
        if (type == STRING64) { return "VARCHAR(64)"; } // TODO (at least in certain dialects): COLLATE utf16_bin
        if (type == STRING128) { return "VARCHAR(128)"; } // TODO (at least in certain dialects): COLLATE utf16_bin
        if (type == STRING) { return "TEXT"; } // TODO (at least in certain dialects): COLLATE utf16_bin
        if (type == BINARY128) { return "BINARY(16)"; }
        if (type == BINARY256) { return "BINARY(32)"; }
        if (type == BINARY) { return "BLOB"; } // TODO: It was "MEDIUMBLOB" before and I have no idea why.
        throw CaseExceptionBuilder.withVariable("type").withValue(type).build();
    }
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(getTypeInSQL());
    }
    
}
