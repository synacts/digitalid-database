package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

/**
 *
 */
public class SQLNotNullConstraint extends SQLColumnDefinition {
    
    SQLNotNullConstraint() {}
    
    @Override
    public void getColumnDefinition(@Nonnull @NonCapturable StringBuilder string) throws InternalException {
        string.append("NOT NULL");
    }
    
}
