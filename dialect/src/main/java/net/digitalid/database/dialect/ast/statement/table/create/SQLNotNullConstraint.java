package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.exceptions.InternalException;


/**
 * This SQL node represents a not-null constraint on the column.
 */
public class SQLNotNullConstraint extends SQLColumnDefinition {
    
    @Pure
    @Override
    public void getColumnDefinition(@Nonnull @NonCaptured @Modified StringBuilder string) throws InternalException {
        string.append("NOT NULL");
    }
    
}
