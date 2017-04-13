package net.digitalid.database.dialect.statement.schema;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.statement.SQLStatementNode;
import net.digitalid.database.unit.Unit;

/**
 * An SQL create schema statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLCreateSchemaStatement extends SQLStatementNode {
    
    /* -------------------------------------------------- Schema -------------------------------------------------- */
    
    // TODO: Should the schema name rather be passed explicitly instead of deriving it from the unit during unparsing?
    
//    /**
//     * Returns the schema which is to be created by this statement.
//     */
//    @Pure
//    public @Nonnull SQLSchemaName getSchema();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(unit.getName()).build();
        string.append("CREATE SCHEMA IF NOT EXISTS ");
        dialect.unparse(schemaName, unit, string);
    }
    
}
