package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;

/**
 * SQL rows.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLRows extends SQLValues {
    
    /* -------------------------------------------------- Rows -------------------------------------------------- */
    
    /**
     * Returns the rows whose evaluations are to be inserted.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLExpressions> getRows();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("VALUES ");
        dialect.unparse(getRows(), unit, string);
    }
    
}
