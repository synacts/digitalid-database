package net.digitalid.database.conversion.columndeclarations;

import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.size.MinSize;

/**
 * Provides 
 */
public class SQLOrderedStatements<S, I extends SQLColumnDeclarations<I, ?, S>> {
    
    private final @MinSize(1) @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderByColumn;
    
    private final @Nonnull @MinSize(1) FreezableArrayList<@Nonnull S> statementsOrderedByExecution;
    
    private final @Nonnull ReadOnlyList<@Nonnull Integer> columnCountForGroup;
    
    @Impure
    private @Nonnull @MinSize(0) FreezableArrayList<@Nonnull S> orderByExecution(@Nonnull I columnDeclarations, @Nonnull Integer alreadyProcessedStatements) {
        final @Nonnull Map<@Nonnull String, @Nonnull I> referencedColumnDeclarations = columnDeclarations.getReferencedTablesColumnDeclarations();
        final @Nonnull FreezableArrayList<@Nonnull S> orderedStatements = FreezableArrayList.withNoElements();
        for (Map.Entry<@Nonnull String, @Nonnull I> referencedColumnDeclarationEntry : referencedColumnDeclarations.entrySet()) {
            final @Nonnull I referencedColumnDeclaration = referencedColumnDeclarationEntry.getValue();
            orderedStatements.addAll(orderByExecution(referencedColumnDeclaration, alreadyProcessedStatements));
        }
        orderedStatements.add(columnDeclarations.getStatement());
        int parameterIndex = 0;
        for (Pair<?, Integer> columnDeclarationAndOrder : columnDeclarations.getColumnDeclarationList()) {
            final @Nonnull Integer columnOrder = columnDeclarationAndOrder.get1();
    
            @Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>> statementAndParameterIndex = orderByColumn.get(columnOrder);
            if (statementAndParameterIndex == null) {
                statementAndParameterIndex = FreezableArrayList.withNoElements();
            }
            statementAndParameterIndex.add(Pair.of(alreadyProcessedStatements + orderedStatements.size() - 1, parameterIndex));
            orderByColumn.set(columnOrder, statementAndParameterIndex);
            parameterIndex++;
        }
        alreadyProcessedStatements++;
        final @Nonnull Map<@Nonnull String, @Nonnull I> dependentColumnDeclarations = columnDeclarations.getDependentTablesColumnDeclarations();
        for (Map.Entry<@Nonnull String, @Nonnull I> dependentColumnDeclarationEntry : dependentColumnDeclarations.entrySet()) {
            final @Nonnull I dependentColumnDeclaration = dependentColumnDeclarationEntry.getValue();
            orderedStatements.addAll(orderByExecution(dependentColumnDeclaration, alreadyProcessedStatements));
        }
        return orderedStatements;
    }
    
    @Pure
    private int getAmountOfAllColumns(@Nonnull I columnDeclarations) {
        int amount = 0;
        final @Nonnull Map<@Nonnull String, @Nonnull I> referencedColumnDeclarations = columnDeclarations.getReferencedTablesColumnDeclarations();
        for (Map.Entry<@Nonnull String, @Nonnull I> referencedColumnDeclarationEntry : referencedColumnDeclarations.entrySet()) {
            amount += getAmountOfAllColumns(referencedColumnDeclarationEntry.getValue());
        }
        amount += columnDeclarations.getColumnDeclarationList().size();
        final @Nonnull Map<@Nonnull String, @Nonnull I> dependentColumnDeclarations = columnDeclarations.getDependentTablesColumnDeclarations();
        for (Map.Entry<@Nonnull String, @Nonnull I> dependentColumnDeclarationEntry : dependentColumnDeclarations.entrySet()) {
            amount += getAmountOfAllColumns(dependentColumnDeclarationEntry.getValue());
        }
        return amount;
    }
    
    protected SQLOrderedStatements(@Nonnull I columnDeclarations) {
        int numColumns = getAmountOfAllColumns(columnDeclarations);
        this.orderByColumn = FreezableArrayList.withCapacity(numColumns);
        for (int i = 0; i < numColumns; i++) {
            orderByColumn.add(null);
        }
        this.statementsOrderedByExecution = orderByExecution(columnDeclarations, 0);
        
        int numInitializedColumns = 0;
        for (int i = 0; i < orderByColumn.size(); i++) {
            if (orderByColumn.get(i) != null) {
                numInitializedColumns += orderByColumn.get(i).size();
            } else {
                orderByColumn.remove(i);
            }
        }
        Require.that(numInitializedColumns == numColumns).orThrow("Not all columns have been initialized.");
        this.columnCountForGroup = columnDeclarations.getColumnCountForGroup();
    }
    
    /**
     * Ordered by statement execution.
     */
    @Pure
    public @Nonnull @MinSize(1) ReadOnlyList<@Nonnull S> getStatementsOrderedByExecution() {
        return statementsOrderedByExecution;
    }
    
    @Pure
    public @Nonnull @MinSize(1) FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> getOrderByColumn() {
        return orderByColumn;
    }
    
    @Pure
    public @Nonnull ReadOnlyList<@Nonnull Integer> getColumnCountForGroup() {
        return columnCountForGroup;
    }
}
