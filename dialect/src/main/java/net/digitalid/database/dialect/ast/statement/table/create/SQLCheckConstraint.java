package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLExpression;

/**
 *
 */
public abstract class SQLCheckConstraint extends SQLColumnConstraint {
    
    
    protected abstract @Nonnull SQLExpression<?> getCheckConstraint();
    
    @Override
    public void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCaptured StringBuilder string) throws InternalException {
        string.append("CHECK (");
        dialect.transcribe(site, string, getCheckConstraint(), false);
        string.append(")");
    }
    
}
