package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.exceptions.InternalException;

/**
 * Description.
 */
public class SQLDefaultValueConstraint extends SQLColumnDefinition {
    
    private final @Nonnull String defaultValue;
    
    SQLDefaultValueConstraint(@Nonnull CustomAnnotation defaultValue) {
        this.defaultValue = defaultValue.get("value", String.class);
    }
    
    @Pure
    @Override
    public void getColumnDefinition(@Nonnull @NonCaptured StringBuilder string) throws InternalException {
        string.append("DEFAULT ");
        string.append(defaultValue.toUpperCase());
    }
    
}
