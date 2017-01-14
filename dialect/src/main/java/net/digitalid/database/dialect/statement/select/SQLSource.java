package net.digitalid.database.dialect.statement.select;

import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.SQLParameterizableNode;

/**
 * The source of the SQL select statement node.
 */
public interface SQLSource<T extends SQLNode<T>> extends SQLParameterizableNode<T> {
    
}
