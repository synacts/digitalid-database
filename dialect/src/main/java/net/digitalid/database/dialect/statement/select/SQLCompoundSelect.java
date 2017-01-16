package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * Description.
 */
@Immutable
public interface SQLCompoundSelect extends SQLUnorderedSelect {
    
    /**
     * An optional compound operator.
     */
    @Pure
    public @Nonnull SQLCompoundOperator getOperator();
    
}
