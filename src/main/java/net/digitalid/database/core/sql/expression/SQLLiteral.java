package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.core.sql.expression.number.SQLNumberLiteral;
import net.digitalid.database.core.sql.expression.string.SQLStringLiteral;
import net.digitalid.utility.annotations.reference.NonCapturable;

/**
 * This class implements a literal value.
 * 
 * @see SQLBooleanLiteral
 * @see SQLNumberLiteral
 * @see SQLStringLiteral
 */
public abstract class SQLLiteral implements SQLExpression {
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {}
    
}
