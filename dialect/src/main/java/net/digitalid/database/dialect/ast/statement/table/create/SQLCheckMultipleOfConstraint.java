package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLBinaryNumberExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.utility.validation.annotations.math.MultipleOf;

/**
 *
 */
public class SQLCheckMultipleOfConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckMultipleOfConstraint(@Nonnull Field field) {
        assert field.isAnnotationPresent(MultipleOf.class) : "The annotation @MultipleOf is present.";
        @Nonnull MultipleOf multipleOf = field.getAnnotation(MultipleOf.class);
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.EQUAL,
                SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MODULO, SQLNumberReference.get(field), SQLNumberLiteral.get(multipleOf.value())), SQLNumberLiteral.get(0L));
    }
    
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
}
