package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.math.MultipleOf;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLBinaryNumberExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 *
 */
public class SQLCheckMultipleOfConstraint extends SQLCheckConstraint {
    
    private final @Nonnull SQLExpression checkConstraint;
    
    private final long multipleOfValue;
    
    SQLCheckMultipleOfConstraint(@Nonnull Annotation annotation, @Nonnull String columnName) {
        Require.that(annotation instanceof MultipleOf).orThrow("The annotation @MultipleOf is present.");
        
        @Nullable MultipleOf multipleOf = (MultipleOf) annotation; 
        this.multipleOfValue = multipleOf.value();
        checkConstraint = SQLNumberComparisonBooleanExpression.get(SQLComparisonOperator.EQUAL,
                SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MODULO, SQLNumberReference.get(columnName), SQLNumberLiteral.get(multipleOfValue)), SQLNumberLiteral.get(0L));
    }
    
    @Override
    protected @Nonnull SQLExpression<?> getCheckConstraint() {
        return checkConstraint;
    }
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        collector.setInteger64(multipleOfValue);
        collector.setInteger64(0L);
    }
    
}
