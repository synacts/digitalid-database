package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL parameter unparses to a question mark.
 */
@Immutable
@GenerateSubclass
public interface SQLParameter extends SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    /**
     * Stores an instance of the surrounding class.
     */
    public static final @Nonnull SQLParameter INSTANCE = new SQLParameterSubclass();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("?");
    }
    
}
