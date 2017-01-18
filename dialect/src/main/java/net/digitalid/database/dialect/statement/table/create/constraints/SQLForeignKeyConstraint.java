package net.digitalid.database.dialect.statement.table.create.constraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.table.create.SQLReference;
import net.digitalid.database.subject.site.Site;

/**
 * A foreign key constraint in a create table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
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
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        SQLColumnsConstraint.super.unparse(dialect, site, string);
        string.append(" FOREIGN KEY (");
        dialect.unparse(getColumns(), site, string);
        string.append(")");
        dialect.unparse(getReference(), site, string);
    }
    
    /* -------------------------------------------------- Utility -------------------------------------------------- */
    
    // TODO: Delete the following code once the foreign key constraint can be derived from a custom annotation.
    
//    @Pure
//    @Override
//    public @Nonnull String getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException {
//        final @Nonnull CustomAnnotation references = null; // This was a field.
//        final @Nonnull StringBuilder string = new StringBuilder();
//        //string.append("FOREIGN KEY REFERENCES ");
//        string.append("REFERENCES ");
//        string.append(references.get("foreignTable", String.class));
//        string.append(" (");
//        string.append(references.get("columnName", String.class));
//        string.append(") ON DELETE ");
//        final @Nonnull String onDelete;
//        if (references.get("onDelete", ForeignKeyAction.class) != null) {
//            onDelete = references.get("onDelete", ForeignKeyAction.class).value;
//        } else {
//            onDelete = ForeignKeyAction.RESTRICT.value;
//        }
//        string.append(onDelete);
//        string.append(" ON UPDATE ");
//        final @Nonnull String onUpdate;
//        if (references.get("onUpdate", ForeignKeyAction.class) != null) {
//            onUpdate = references.get("onUpdate", ForeignKeyAction.class).value;
//        } else {
//            onUpdate = ForeignKeyAction.RESTRICT.value;
//        }
//        string.append(onUpdate);
//        return string.toString();
//    }
    
}