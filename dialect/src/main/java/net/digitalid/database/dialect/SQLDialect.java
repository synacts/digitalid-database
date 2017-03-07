package net.digitalid.database.dialect;

import java.util.Iterator;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.logging.logger.Logger;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.unit.Unit;

/**
 * A dialect implements a particular version of the structured query language (SQL).
 */
@Stateless
@GenerateSubclass
public abstract class SQLDialect {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    /**
     * Stores the dialect configuration of the database.
     */
    public static final @Nonnull Configuration<SQLDialect> instance = Configuration.<SQLDialect>with(new SQLDialectSubclass()).addDependency(Logger.logger);
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    /**
     * Appends the given node as SQL in this dialect at the given unit to the given string.
     * Override this method in specific dialects to avoid the default implementation of certain nodes with instance checks.
     */
    @Pure
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        node.unparse(this, unit, string);
    }
    
    /**
     * Appends the given nodes separated by commas as SQL in this dialect at the given unit to the given string.
     */
    @Pure
    public void unparse(@Nonnull Iterable<? extends SQLNode> nodes, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        final @Nonnull Iterator<? extends SQLNode> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            unparse(iterator.next(), unit, string);
            if (iterator.hasNext()) { string.append(", "); }
        }
    }
    
    /* -------------------------------------------------- Utility -------------------------------------------------- */
    
    /**
     * Returns the given node as SQL in this dialect at the given unit.
     */
    @Pure
    public static @Nonnull @SQLFraction String unparse(@Nonnull SQLNode node, @Nonnull Unit unit) {
        final @Nonnull StringBuilder result = new StringBuilder();
        instance.get().unparse(node, unit, result);
        return result.toString();
    }
    
}
