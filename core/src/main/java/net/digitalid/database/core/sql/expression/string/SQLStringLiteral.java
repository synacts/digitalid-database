package net.digitalid.database.core.sql.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLLiteral;
import net.digitalid.database.core.table.Site;

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
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {}
    
}
