package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;

/**
 *
 */
public abstract class SQLCheckConstraint extends SQLColumnConstraint {
    
    
    protected abstract @Nonnull SQLExpression<?> getCheckConstraint();
    
    @Override
    public void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        string.append("CHECK (");
        getCheckConstraint().getTranscriber().transcribeNode(dialect, getCheckConstraint(), site, string);
        string.append(")");
    }
    
}
