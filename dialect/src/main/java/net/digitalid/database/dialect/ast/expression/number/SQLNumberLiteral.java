package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLLiteral;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;

/**
 * This class implements a number literal.
 */
public class SQLNumberLiteral extends SQLNumberExpression implements SQLLiteral {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this number literal.
     */
    public final long value;
    
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
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLNumberLiteral> transcriber = new Transcriber<SQLNumberLiteral>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLNumberLiteral node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            string.append(node.value);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLNumberLiteral> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {}
    
}
