package net.digitalid.database.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.unit.Unit;

/**
 * This class implements the H2 dialect.
 */
@Stateless
@GenerateSubclass
public abstract class H2Dialect extends SQLDialect {
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new H2DialectSubclass());
    }
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLIdentifier) {
            final @Nonnull SQLIdentifier identifier = (SQLIdentifier) node;
            string.append(identifier.getString());
        } else if (node instanceof SQLInsertStatement) {
            final @Nonnull SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) node;
            if (sqlInsertStatement.isReplacing()) {
                final @Nonnull SQLDialect dialect = SQLDialect.instance.get();
                string.append("MERGE INTO ");
                dialect.unparse(sqlInsertStatement.getTable(), unit, string);
                string.append(" (");
                dialect.unparse(sqlInsertStatement.getColumns(), unit, string);
                string.append(") ");
                dialect.unparse(sqlInsertStatement.getValues(), unit, string);
            } else {
                super.unparse(node, unit, string);
            }
        } else {
            super.unparse(node, unit, string);
        }
    }
    
}
