package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;

/**
 *
 */
public class SQLPrimaryKeyConstraint extends SQLColumnConstraint {
    
    @Override
    public void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        string.append("PRIMARY KEY");
    }
    
}
