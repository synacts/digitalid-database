package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.utility.validation.math.NonPositive;

/**
 *
 */
public class SQLCheckNonPositiveConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckNonPositiveConstraint(@Nonnull Field field) {
        assert field.isAnnotationPresent(NonPositive.class) : "The annotation @NonPositive is present.";
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.LESS_OR_EQUAL, SQLNumberReference.get(field), SQLNumberLiteral.get(0L));
    }
    
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
}
