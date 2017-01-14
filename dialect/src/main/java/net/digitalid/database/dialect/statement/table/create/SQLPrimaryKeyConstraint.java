package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLEncoder;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents a primary key constraint.
 */
public class SQLPrimaryKeyConstraint extends SQLColumnConstraint {
    
    /* -------------------------------------------------- Constraint Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull String getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException {
        return "PRIMARY KEY";
    }
    
    /* -------------------------------------------------- SQL Parameterized Node -------------------------------------------------- */
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLEncoder collector) throws FailedSQLValueConversionException {}
    
}
