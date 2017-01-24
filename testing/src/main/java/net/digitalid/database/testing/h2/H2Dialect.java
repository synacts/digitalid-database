package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.unit.Unit;

@Stateless
public class H2Dialect extends SQLDialect {
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new H2Dialect());
    }
    
    @Pure
    @Override
    @TODO(task = "Steffi thinks that identifiers have to be in upper case. According to http://www.h2database.com/html/grammar.html#quoted_name, however, the default unparsing seems fine.", date = "2017-01-18", author = Author.KASPAR_ETTER)
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLIdentifier) {
            final @Nonnull SQLIdentifier identifier = (SQLIdentifier) node;
            string.append(identifier.getString().toUpperCase());
        } else {
            super.unparse(node, unit, string);
        }
    }
    
}
