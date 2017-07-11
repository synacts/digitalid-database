package net.digitalid.database.dialect.statement.table.create.constraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.table.create.SQLReference;

/**
 * A foreign key constraint in a create table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
@TODO(task = "The delete and update actions already exist in SQLReference!", date = "2017-04-16", author = Author.KASPAR_ETTER)
public interface SQLForeignKeyConstraint extends SQLColumnsConstraint {
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
    
    /**
     * Returns the reference of this foreign key constraint.
     */
    @Pure
    public @Nonnull SQLReference getReference();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        SQLColumnsConstraint.super.unparse(dialect, unit, string);
        string.append(" FOREIGN KEY (");
        dialect.unparse(getColumns(), unit, string);
        string.append(")");
        dialect.unparse(getReference(), unit, string);
    }
    
}
