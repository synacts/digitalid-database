package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.annotations.Default;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;

/**
 * Description.
 */
public class SQLDefaultValueConstraint extends SQLColumnConstraint {
    
    private final @Nonnull String defaultValue;
    
    SQLDefaultValueConstraint(@Nonnull Default defaultValue) {
        this.defaultValue = defaultValue.value();
    }
    
    @Override
    public void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        string.append("DEFAULT ");
        string.append(defaultValue);
    }
    
}
