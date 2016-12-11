package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents a unique constraint on a column.
 */
public class SQLUniqueConstraint extends SQLColumnConstraint {
    
    @Pure
    @Override
    public @Nonnull String getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException {
        return "UNIQUE";
    }
    
    /* -------------------------------------------------- SQLParameterized Node -------------------------------------------------- */
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {}
}
