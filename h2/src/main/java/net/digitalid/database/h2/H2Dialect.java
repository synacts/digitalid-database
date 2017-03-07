package net.digitalid.database.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.statement.insert.SQLConflictClause;
import net.digitalid.database.unit.Unit;

/**
 * This class implements the H2 dialect.
 */
@Stateless
@GenerateSubclass
public abstract class H2Dialect extends SQLDialect {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new H2DialectSubclass());
    }
    
    /* -------------------------------------------------- Unparsing -------------------------------------------------- */
    
    @Pure
    protected void unparse(@Nonnull SQLIdentifier identifier, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(identifier.getString());
    }
    
    @Pure
    @TODO(task = "Should we throw an exception in the other cases?", date = "2017-03-07", author = Author.KASPAR_ETTER)
    protected void unparse(@Nonnull SQLConflictClause conflictClause, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (conflictClause == SQLConflictClause.REPLACE) { string.append("MERGE"); }
        else { string.append("INSERT"); }
    }
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLIdentifier) { unparse((SQLIdentifier) node, unit, string); }
        else if (node instanceof SQLConflictClause) { unparse((SQLConflictClause) node, unit, string); }
        else { super.unparse(node, unit, string); }
    }
    
}
