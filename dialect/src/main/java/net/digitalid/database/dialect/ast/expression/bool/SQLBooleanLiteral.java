package net.digitalid.database.dialect.ast.expression.bool;

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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBooleanLiteral node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            if (node.value != null) {
                string.append(node.value ? "TRUE" : "FALSE");
            } else {
                string.append("NULL");
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLBooleanLiteral> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        collector.setBoolean(value);
    }
    
}
