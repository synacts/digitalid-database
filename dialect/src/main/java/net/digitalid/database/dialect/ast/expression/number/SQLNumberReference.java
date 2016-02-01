package net.digitalid.database.dialect.ast.expression.number;

import java.lang.reflect.Field;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLIdentifier;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.validator.Validator;

/**
 *
 */
public class SQLNumberReference extends SQLNumberExpression<SQLNumberReference> implements SQLIdentifier<SQLNumberReference> {
    
    private final @Nonnull SQLQualifiedColumnName qualifiedColumnName;
    
    private final static @Nonnull @NonNullableElements Class<?>[] acceptedTypes = new Class<?>[] {byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class, BigInteger.class};
    
    private SQLNumberReference(@Nonnull SQLQualifiedColumnName qualifiedColumnName) {
        this.qualifiedColumnName = qualifiedColumnName;
    }
    
    public static @Nonnull SQLNumberReference get(@Nonnull Field field) {
        Validator.checkType(field, acceptedTypes);
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), null);
        return new SQLNumberReference(qualifiedColumnName);
    }
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        // TODO: implement
    }
    
    @Override
    public @Nonnull String getValue() {
        return qualifiedColumnName.getValue();
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLNumberReference> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    public @Nonnull Transcriber<SQLNumberReference> getTranscriber() {
        return transcriber;
    }
    
}
