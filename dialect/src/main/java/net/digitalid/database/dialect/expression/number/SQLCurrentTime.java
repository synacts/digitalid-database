package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.unit.Unit;

/**
 * The current time as milliseconds since 1970.
 */
@Immutable
@GenerateSubclass
public interface SQLCurrentTime extends SQLNumberExpression {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * Stores an instance of the surrounding type.
     */
    public final @Nonnull SQLCurrentTime INSTANCE = new SQLCurrentTimeSubclass();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("TIMESTAMP()");
    }
    
}
