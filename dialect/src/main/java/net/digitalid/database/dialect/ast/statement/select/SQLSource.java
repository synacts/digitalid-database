package net.digitalid.database.dialect.ast.statement.select;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;

/**
 * The source of the SQL select statement node.
 */
public interface SQLSource<T extends SQLNode<T>> extends SQLParameterizableNode<T> {
    
}
