package net.digitalid.database.core.sql.statement.insert;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 *
 */
public class SQLValues implements SQLParameterizableNode, SQLValuesOrStatement {
   
    private final @Nonnull @NonNullableElements FreezableArrayList<SQLExpression> values;
    
    private SQLValues() {
        this.values = FreezableArrayList.get();
    }
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        for (@Nonnull SQLExpression sqlExpression : values) {
            // in the simplest case, the sqlExpression is a literal.
            sqlExpression.storeValues(collector);
        }
    }
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
