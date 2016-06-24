package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.validation.annotations.math.NonNegative;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 *
 */
public class SQLCheckNonNegativeConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckNonNegativeConstraint(@Nonnull Annotation annotation, @Nonnull String columnName) {
        Require.that(annotation instanceof NonNegative).orThrow("The annotation @NonNegative is present.");
        
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.GREATER_OR_EQUAL, SQLNumberReference.get(columnName), SQLNumberLiteral.get(0L));
    }
    
    @Pure
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        collector.setInteger64(0L);
    }
    
}
