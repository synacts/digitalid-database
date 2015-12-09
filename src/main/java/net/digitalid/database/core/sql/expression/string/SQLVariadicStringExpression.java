package net.digitalid.database.core.sql.expression.string;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.utility.annotations.reference.Captured;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a variadic string expression.
 */
@Immutable
public class SQLVariadicStringExpression extends SQLVariadicExpression<SQLVariadicStringOperator, SQLStringExpression> {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new variadic string expression with the given parameters.
     * 
     * @param operator the string operator of the new variadic expression.
     * @param expressions the string expressions of the new variadic expression.
     */
    protected SQLVariadicStringExpression(@Nonnull SQLVariadicStringOperator operator, @Captured @Nonnull SQLStringExpression... expressions) {
        super(operator, expressions);
    }
    
    /**
     * Returns a new variadic string expression with the given parameters.
     * 
     * @param operator the string operator of the new variadic expression.
     * @param expressions the string expressions of the new variadic expression.
     * 
     * @return a new variadic string expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLVariadicStringExpression get(@Nonnull SQLVariadicStringOperator operator, @Captured @Nonnull SQLStringExpression... expressions) {
        return new SQLVariadicStringExpression(operator, expressions);
    }
    
}
