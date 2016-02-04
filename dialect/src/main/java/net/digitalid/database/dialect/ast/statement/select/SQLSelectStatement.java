package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.castable.exceptions.InvalidClassCastException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.statement.insert.SQLValuesOrStatement;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * Description.
 */
public class SQLSelectStatement implements SQLParameterizableNode<SQLSelectStatement>, SQLValuesOrStatement<SQLSelectStatement> {
    
    private SQLSelectStatement() {}
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        // TODO
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLSelectStatement> transcriber = new Transcriber<SQLSelectStatement>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLSelectStatement node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            // TODO: transcribe!
        }
        
    };
 
    @Override
    public @Nonnull Transcriber<SQLSelectStatement> getTranscriber() {
        return transcriber;
    }
    
    
    @Override
    public @Nonnull <T> T castTo(@Nonnull Class<T> targetClass) throws InvalidClassCastException {
        assert targetClass.isInstance(this) : "This object can only be casted to SQLSelectStatement";
        return (T) this;
    }
}
