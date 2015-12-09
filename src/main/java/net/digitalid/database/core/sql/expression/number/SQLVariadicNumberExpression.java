package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.utility.annotations.reference.Captured;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a variadic number expression.
 */
@Immutable
public class SQLVariadicNumberExpression extends SQLVariadicExpression<SQLVariadicNumberOperator, SQLNumberExpression> implements SQLNumberExpression {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new variadic number expression with the given parameters.
     * 
     * @param operator the number operator of the new variadic expression.
     * @param expressions the number expressions of the new variadic expression.
     */
    protected SQLVariadicNumberExpression(@Nonnull SQLVariadicNumberOperator operator, @Captured @Nonnull SQLNumberExpression... expressions) {
        super(operator, expressions);
    }
    
    /**
     * Returns a new variadic number expression with the given parameters.
     * 
     * @param operator the number operator of the new variadic expression.
     * @param expressions the number expressions of the new variadic expression.
     * 
     * @return a new variadic number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLVariadicNumberExpression get(@Nonnull SQLVariadicNumberOperator operator, @Captured @Nonnull SQLNumberExpression... expressions) {
        return new SQLVariadicNumberExpression(operator, expressions);
    }
    
}
