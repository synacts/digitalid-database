package net.digitalid.database.core.sql.expression.string;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLLiteral;
import net.digitalid.database.core.sql.expression.bool.SQLBooleanExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class implements a string literal.
 */
public class SQLStringLiteral extends SQLLiteral implements SQLBooleanExpression {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this string literal.
     */
    private final @Nonnull String value;
    
    /**
     * Returns the value of this string literal.
     * 
     * @return the value of this string literal.
     */
    @Pure
    public final @Nonnull String getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new string literal with the given value.
     * 
     * @param value the value of the new string literal.
     */
    protected SQLStringLiteral(@Nonnull String value) {
        this.value = value;
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
