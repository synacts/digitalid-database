package net.digitalid.database.conversion.collectors;

import java.util.Queue;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;

import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;

/**
 *
 */
public class SQLOrderedInsertStatements {
    
    final @Nonnull FreezableArrayList<@Nonnull SQLInsertStatement> insertStatements = FreezableArrayList.withNoElements();
    final @Nonnull FreezableHashMap<@Nonnull SQLInsertDeclaration, @Nonnull Integer> processedDeclarations = FreezableHashMap.withDefaultCapacity();
    final @Nonnull FreezableLinkedList<@Nonnull Integer> order = FreezableLinkedList.withNoElements();
    
    protected SQLOrderedInsertStatements(@Nonnull ReadOnlyList<SQLInsertDeclaration> orderedInsertDeclarations, @Nonnull SQLQualifiedTableName qualifiedTableName) {
        for (@Nonnull SQLInsertDeclaration orderedInsertDeclaration : orderedInsertDeclarations) {
            if (!processedDeclarations.containsKey(orderedInsertDeclaration)) {
                insertStatements.add(SQLInsertStatement.get(qualifiedTableName, orderedInsertDeclaration.getColumnNames()));
                processedDeclarations.put(orderedInsertDeclaration, insertStatements.size() - 1);
            }
            order.add(processedDeclarations.get(orderedInsertDeclaration));
        }
    }
    
    @Pure
    public @Nonnull ReadOnlyList<@Nonnull SQLInsertStatement> getInsertStatements() {
        return insertStatements;
    }
    
    @Pure
    public @Nonnull Queue<@Nonnull Integer> getOrder() {
        return order;
    }
    
}
