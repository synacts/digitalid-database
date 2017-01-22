package net.digitalid.database.dialect.identifier.column;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.table.SQLTable;
import net.digitalid.database.unit.Unit;

/**
 * An SQL qualified column.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLQualifiedColumn extends SQLColumn {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table of this qualified column.
     */
    @Pure
    public @Nonnull SQLTable getTable();
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Returns the column of this qualified column.
     */
    @Pure
    public @Nonnull SQLColumnName getColumn();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getTable(), unit, string);
        string.append(".");
        dialect.unparse(getColumn(), unit, string);
    }
    
}
