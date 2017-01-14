package net.digitalid.database.dialect;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLOperator;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.subject.site.Site;

/**
 * All SQL syntax tree nodes implement this interface.
 * 
 * @see SQLExpression
 * @see SQLOperator
 * @see SQLIdentifier
 * @see SQLTable
 */
@Immutable
public interface SQLNode extends RootInterface {
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    /**
     * Appends this node as SQL in the given dialect at the given site to the given string.
     */
    @Pure
    public void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string);
    
}
