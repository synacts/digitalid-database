package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLLiteral;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This class implements a boolean literal.
 */
public class SQLBooleanLiteral extends SQLBooleanExpression implements SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this boolean literal.
     */
    public final Boolean value;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new boolean literal with the given value.
     * 
     * @param value the value of the new boolean literal.
     */
    protected SQLBooleanLiteral(Boolean value) {
        this.value = value;
    }
    
    /**
     * Returns a new boolean literal with the given value.
     * 
     * @param value the value of the new boolean literal.
     * 
     * @return a new boolean literal with the given value.
     */
    @Pure
    public static @Nonnull SQLBooleanLiteral get(Boolean value) {
        return new SQLBooleanLiteral(value);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLBooleanLiteral> transcriber = new Transcriber<SQLBooleanLiteral>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBooleanLiteral node, @Nonnull Site site) throws InternalException {
            return Boolean.toString(node.value).toUpperCase();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLBooleanLiteral> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public final void storeValues(@Nonnull @NonCaptured @Modified SQLValueCollector collector) throws FailedSQLValueConversionException {
        collector.setBoolean(value);
    }
    
}