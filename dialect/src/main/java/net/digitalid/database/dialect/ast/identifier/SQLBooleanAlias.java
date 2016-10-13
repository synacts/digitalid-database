package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.size.MaxSize;

import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;

/**
 *
 */
public final class SQLBooleanAlias extends SQLBooleanExpression<SQLBooleanAlias> implements SQLAlias<SQLBooleanAlias> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    private final @Nonnull @MaxSize(63) String value;
    
    @Pure
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        return value;
    }
    
    /**
     * Creates a new SQL alias with the given value.
     *
     * @param value the value of the new SQL alias.
     */
    protected SQLBooleanAlias(@Nonnull @MaxSize(63) String value) {
        this.value = value;
    }
    
    @Pure
    public static @Nonnull SQLBooleanAlias with(@Nonnull @MaxSize(63) String value) {
        return new SQLBooleanAlias(value);
    }
    
    /* -------------------------------------------------- SQL Values -------------------------------------------------- */
    
    @Pure
    @Override
    @Deprecated
    public void storeValues(@NonCaptured @Modified @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        
    }
    
   /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLBooleanAlias> transcriber = new SQLIdentifierTranscriber<>();
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLBooleanAlias> getTranscriber() {
        return transcriber;
    }
    
}
