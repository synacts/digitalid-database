package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.unit.Unit;

/**
 * An SQL insert statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLInsertStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the columns into which the given values are inserted.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> getColumns();
    
    /* -------------------------------------------------- Values -------------------------------------------------- */
    
    /**
     * Returns the values which are inserted into the given columns.
     */
    @Pure
    public @Nonnull SQLValues getValues();
    
    /* -------------------------------------------------- Replace -------------------------------------------------- */
    
    /**
     * Returns whether an entry with the same primary key shall be replaced with the given values.
     */
    @Pure
    @Default("false")
    public boolean isReplacing();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("INSERT");
        if (isReplacing()) { string.append(" OR REPLACE"); }
        string.append(" INTO ");
        dialect.unparse(getTable(), unit, string);
        string.append(" (");
        dialect.unparse(getColumns(), unit, string);
        string.append(") ");
        dialect.unparse(getValues(), unit, string);
    }
    
}
