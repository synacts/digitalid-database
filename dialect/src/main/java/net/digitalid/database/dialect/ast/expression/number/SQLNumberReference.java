package net.digitalid.database.dialect.ast.expression.number;

import java.math.BigInteger;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLIdentifier;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 *
 */
public class SQLNumberReference extends SQLNumberExpression<SQLNumberReference> implements SQLIdentifier<SQLNumberReference> {
    
    private final @Nonnull SQLQualifiedColumnName qualifiedColumnName;
    
    private final static @Nonnull @NonNullableElements Class<?>[] acceptedTypes = new Class<?>[] {byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class, BigInteger.class};
    
    private SQLNumberReference(@Nonnull SQLQualifiedColumnName qualifiedColumnName) {
        this.qualifiedColumnName = qualifiedColumnName;
    }
    
    @Pure
    public static @Nonnull SQLNumberReference get(@Nonnull String columnName) {
        //Validator.checkType(field, acceptedTypes);
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(columnName, null);
        return new SQLNumberReference(qualifiedColumnName);
    }
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        // TODO: implement
    }
    
    @Pure
    @Override
    public @Nonnull String getValue() {
        return qualifiedColumnName.getValue();
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLNumberReference> transcriber = new SQLIdentifierTranscriber<>();
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLNumberReference> getTranscriber() {
        return transcriber;
    }
    
}
