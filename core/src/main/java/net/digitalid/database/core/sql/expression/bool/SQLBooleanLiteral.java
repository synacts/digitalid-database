package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLLiteral;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * This class implements a boolean literal.
 */
public class SQLBooleanLiteral extends SQLBooleanExpression implements SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this boolean literal.
     */
    private final boolean value;
    
    /**
     * Returns the value of this boolean literal.
     * 
     * @return the value of this boolean literal.
     */
    @Pure
    public final boolean getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new boolean literal with the given value.
     * 
     * @param value the value of the new boolean literal.
     */
    protected SQLBooleanLiteral(boolean value) {
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
    public static @Nonnull SQLBooleanLiteral get(boolean value) {
        return new SQLBooleanLiteral(value);
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
