package net.digitalid.database.dialect.ast.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.storage.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLLiteral;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This class implements a string literal.
 */
public class SQLStringLiteral extends SQLStringExpression implements SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this string literal.
     */
    private final @Nonnull String value;
    
    /**
     * Returns the value of this string literal.
     * 
     * @return the value of this string literal.
     */
    @Pure
    public final @Nonnull String getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new string literal with the given value.
     * 
     * @param value the value of the new string literal.
     */
    protected SQLStringLiteral(@Nonnull String value) {
        this.value = value;
    }
    
    /**
     * Returns a new string literal with the given value.
     * 
     * @param value the value of the new string literal.
     * 
     * @return a new string literal with the given value.
     */
    @Pure
    public static @Nonnull SQLStringLiteral get(@Nonnull String value) {
        return new SQLStringLiteral(value);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLStringLiteral> transcriber = new Transcriber<SQLStringLiteral>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLStringLiteral node, @Nonnull Site site)  throws InternalException {
            return node.value;
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLStringLiteral> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public final void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        collector.setString(value);
    }
    
}
