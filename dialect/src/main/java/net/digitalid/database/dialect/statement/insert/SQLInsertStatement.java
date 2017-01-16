package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.statement.SQLStatement;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL insert statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLInsertStatement extends SQLStatement {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the columns into which the given values are inserted.
     */
    @Pure
    public @Nonnull ImmutableList<@Nonnull SQLColumnName> getColumns();
    
    /* -------------------------------------------------- Values -------------------------------------------------- */
    
    /**
     * Returns the values which are inserted into the given columns.
     */
    @Pure
    public @Nonnull SQLValues getValues();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("INSERT INTO ");
        dialect.unparse(getTable(), site, string);
        string.append(" (");
        dialect.unparse(getColumns(), site, string);
        string.append(") ");
        dialect.unparse(getValues(), site, string);
    }
    
}
