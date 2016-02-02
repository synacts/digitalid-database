package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.utility.validation.annotations.math.NonNegative;

/**
 *
 */
public class SQLCheckNonNegativeConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckNonNegativeConstraint(@Nonnull Field field) {
        assert field.isAnnotationPresent(NonNegative.class) : "The annotation @NonNegative is present.";
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.GREATER_OR_EQUAL, SQLNumberReference.get(field), SQLNumberLiteral.get(0L));
    }
    
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
}
