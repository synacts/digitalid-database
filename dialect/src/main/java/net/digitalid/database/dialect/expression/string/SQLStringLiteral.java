package net.digitalid.database.dialect.expression.string;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLLiteral;
import net.digitalid.database.subject.site.Site;

/**
 * A string literal.
 * <p>
 * <em>Important:</em> Do not use string literals for user-supplied strings because this method is vulnerable to SQL injections!
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLStringLiteral extends SQLStringExpression, SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the string of this string literal.
     */
    @Pure
    public @Nullable String getString();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(Quotes.inDouble(getString()));
    }
    
}