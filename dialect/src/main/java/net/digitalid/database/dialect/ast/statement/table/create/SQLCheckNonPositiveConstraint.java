package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.validation.annotations.math.NonPositive;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This SQL node represents a check-non-positive constraint.
 */
public class SQLCheckNonPositiveConstraint extends SQLCheckConstraint {
    
    /* -------------------------------------------------- Check Constraint -------------------------------------------------- */
    
    /**
     * The expression for the check constraint.
     */
    private final @Nonnull SQLExpression checkConstraint;
    
    @Pure
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates an SQL check non-positive constraint instance for the {@link NonPositive @NonPositive} annotation and a given column name.
     */
    SQLCheckNonPositiveConstraint(@Nonnull CustomAnnotation annotation, @Nonnull String columnName) {
        Require.that(annotation.getAnnotationType().isAssignableFrom(NonPositive.class)).orThrow("The annotation @NonPositive is present.");
        
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.LESS_OR_EQUAL, SQLNumberReference.get(columnName), SQLNumberLiteral.get(0L));
    }
    
    /* -------------------------------------------------- SQL Parameterized Node -------------------------------------------------- */
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        collector.setInteger64(0L);
    }
    
}
