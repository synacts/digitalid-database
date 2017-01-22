package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.unit.Unit;

/**
 * All SQL operator enumerations implement this interface.
 * 
 * @see SQLUnaryOperator
 * @see SQLBinaryOperator
 * @see SQLVariadicOperator
 */
@Immutable
public interface SQLOperator extends SQLNode {
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    /**
     * Returns the symbol that denotes this operator.
     */
    @Pure
    public @Nonnull String getSymbol();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(getSymbol());
    }
    
}
