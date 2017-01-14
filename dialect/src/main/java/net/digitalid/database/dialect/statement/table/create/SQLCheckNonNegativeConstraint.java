package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.validation.annotations.math.NonNegative;

import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.expression.number.SQLNumberReference;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLEncoder;

/**
 *
 */
public class SQLCheckNonNegativeConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckNonNegativeConstraint(@Nonnull CustomAnnotation annotation, @Nonnull String columnName) {
        Require.that(annotation.getAnnotationType().isAssignableFrom(NonNegative.class)).orThrow("The annotation @NonNegative is present.");
        
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.GREATER_OR_EQUAL, SQLNumberReference.get(columnName), SQLNumberLiteral.get(0L));
    }
    
    @Pure
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLEncoder collector) throws FailedSQLValueConversionException {
        collector.setInteger64(0L);
    }
    
}
