package net.digitalid.database.dialect.statement.table.create.constraints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.constraint.SQLConstraintName;
import net.digitalid.database.subject.site.Site;

/**
 * A constraint in the SQL create table statement.
 * 
 * @see SQLCheckConstraint
 * @see SQLColumnsConstraint
 */
@Immutable
public interface SQLTableConstraint extends SQLNode {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the optional name of this table constraint.
     */
    @Pure
    public @Nullable SQLConstraintName getName();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        final @Nullable SQLConstraintName name = getName();
        if (name != null) {
            string.append("CONSTRAINT ");
            dialect.unparse(name, site, string);
        }
    }
    
}
