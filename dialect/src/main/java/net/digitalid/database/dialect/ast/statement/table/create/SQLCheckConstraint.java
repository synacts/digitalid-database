package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.subject.Site;

/**
 * This SQL node represents an SQL check constraint node.
 */
public abstract class SQLCheckConstraint extends SQLColumnConstraint {
    
    
    /**
     * Returns the SQL expression for the specific check constraint.
     */
    @Pure
    protected abstract @Nonnull SQLExpression<?> getCheckConstraint();
    
    /**
     * Returns the string declaration of the check constraint.
     */
    @Pure
    @Override
    public @Nonnull String getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder string = new StringBuilder();
        string.append("CHECK (");
        string.append(dialect.transcribe(site, getCheckConstraint()));
        string.append(")");
        return string.toString();
    }
    
}
