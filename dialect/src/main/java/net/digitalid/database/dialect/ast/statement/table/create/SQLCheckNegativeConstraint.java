package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.validation.annotations.math.Negative;

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
public class SQLCheckNegativeConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    SQLCheckNegativeConstraint(@Nonnull Annotation annotation, @Nonnull String columnname) {
        Require.that(annotation instanceof Negative).orThrow("The annotation @Negative is present.");
        
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.LESS, SQLNumberReference.get(columnname), SQLNumberLiteral.get(0L));
    }
    
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        collector.setInteger64(0L);
    }
    
}
