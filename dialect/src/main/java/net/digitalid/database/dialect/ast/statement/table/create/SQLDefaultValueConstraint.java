package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.annotations.Default;

/**
 * Description.
 */
public class SQLDefaultValueConstraint extends SQLColumnDefinition {
    
    private final @Nonnull String defaultValue;
    
    SQLDefaultValueConstraint(@Nonnull Default defaultValue) {
        this.defaultValue = defaultValue.value();
    }
    
    @Override
    public void getColumnDefinition(@Nonnull @NonCapturable StringBuilder string) throws InternalException {
        string.append("DEFAULT ");
        string.append(defaultValue);
    }
    
}
