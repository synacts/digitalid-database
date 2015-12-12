package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLLiteral;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.internal.InternalException;

/**
 * This class implements a number literal.
 */
public class SQLNumberLiteral extends SQLNumberExpression implements SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this number literal.
     */
    private final long value;
    
    /**
     * Returns the value of this number literal.
     * 
     * @return the value of this number literal.
     */
    @Pure
    public final long getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new number literal with the given value.
     * 
     * @param value the value of the new number literal.
     */
    protected SQLNumberLiteral(long value) {
        this.value = value;
    }
    
    /**
     * Returns a new number literal with the given value.
     * 
     * @param value the value of the new number literal.
     * 
     * @return a new number literal with the given value.
     */
    @Pure
    public static @Nonnull SQLNumberLiteral get(long value) {
        return new SQLNumberLiteral(value);
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
