package net.digitalid.database.testing.assertion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.enumerations.ForeignKeyAction;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.Database;

import org.junit.Assert;

@GenerateBuilder
@GenerateSubclass
public class ExpectedTableConstraints {
    
    private final @Nonnull FreezableArrayList<@Nonnull ExpectedTableConstraint> expectedTableConstraints = FreezableArrayList.withNoElements();
    
    @Impure
    public void addExpectedResult(@Nonnull ExpectedTableConstraint expectedTableConstraint) {
        expectedTableConstraints.add(expectedTableConstraint);
    }
    
    /* -------------------------------------------------- Foreign Key Action Encoding -------------------------------------------------- */
    
    /**
     * Stores the encoded integer value that identifies the foreign key action.
     */
    private static final @Nonnull FreezableHashMap<@Nonnull ForeignKeyAction, @Nonnull Integer> foreignKeyActionToInteger = FreezableHashMapBuilder.build();
    
    static {
        foreignKeyActionToInteger.put(ForeignKeyAction.CASCADE, 0);
        foreignKeyActionToInteger.put(ForeignKeyAction.RESTRICT, 1);
        foreignKeyActionToInteger.put(ForeignKeyAction.NO_ACTION, 1);
        foreignKeyActionToInteger.put(ForeignKeyAction.SET_NULL, 2);
    }
    
    /**
     * Returns a foreign key action for a given H2 foreign key encoding integer value.
     */
    @Pure
    private @Nonnull ForeignKeyAction getForeignKeyAction(int value) {
        for (Map.Entry<ForeignKeyAction, Integer> entry : foreignKeyActionToInteger.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Failed to find a foreign key action for integer " + value);
    }
    
    /* -------------------------------------------------- Asserts -------------------------------------------------- */
    
    @Pure
    public void assertTableConstraints(@Nonnull Database database) throws DatabaseException {
        for (@Nonnull ExpectedTableConstraint expectedTableConstraint : expectedTableConstraints) {
            final @Nonnull String query = "SELECT PKTABLE_NAME, PKCOLUMN_NAME, FKCOLUMN_NAME, UPDATE_RULE, DELETE_RULE FROM INFORMATION_SCHEMA.CROSS_REFERENCES WHERE FKTABLE_SCHEMA = '" + expectedTableConstraint.getSchema().toUpperCase() + "' AND FKTABLE_NAME = '" + expectedTableConstraint.getTableName().toUpperCase() + "'";
            final @Nonnull ResultSet tableReferencesResult = database.executeQuery(query);
            
            try {
                @Nonnull Set<@Nonnull ExpectedForeignKeyConstraint> expectedForeignKeyConstraintSet = expectedTableConstraint.getForeignKeyConstraints();
                while (tableReferencesResult.next()) {
                    final @Nullable String pkTableName = tableReferencesResult.getString(1);
                    final @Nullable String pkColumnName = tableReferencesResult.getString(2);
                    final @Nullable String fkColumnName = tableReferencesResult.getString(3);
                    final int updateRule = tableReferencesResult.getInt(4);
                    final int deleteRule = tableReferencesResult.getInt(5);
                
                    @Nullable ExpectedForeignKeyConstraint matchingExpectedForeignKeyConstraint = null;
                    for (@Nonnull ExpectedForeignKeyConstraint expectedForeignKeyConstraint : expectedForeignKeyConstraintSet) {
                        if (expectedForeignKeyConstraint.getForeignKeyColumn().equals(fkColumnName)) {
                            matchingExpectedForeignKeyConstraint = expectedForeignKeyConstraint;
                            break;
                        }
                    }
                    Assert.assertNotNull("Foreign key definition for table '" + expectedTableConstraint.getTableName() + "' and foreign key column '" + fkColumnName + "' not found", matchingExpectedForeignKeyConstraint);
                    
                    Assert.assertEquals("Expected reference definition on column '" + matchingExpectedForeignKeyConstraint.getForeignKeyColumn() + "', but got '" + fkColumnName + "'", matchingExpectedForeignKeyConstraint.getForeignKeyColumn(), fkColumnName);
                    Assert.assertEquals("Expected referenced table '" + matchingExpectedForeignKeyConstraint.getReferencedTable().toLowerCase() + "', but got '" + pkTableName + "'", matchingExpectedForeignKeyConstraint.getReferencedTable().toLowerCase(), pkTableName);
                    Assert.assertEquals("Expected referenced column '" + matchingExpectedForeignKeyConstraint.getReferencedColumn() + "', but got '" + pkColumnName + "'", matchingExpectedForeignKeyConstraint.getReferencedColumn(), pkColumnName);
                    if (matchingExpectedForeignKeyConstraint.getDeleteAction() != null) {
                        Assert.assertSame("Expected DELETE action '" + matchingExpectedForeignKeyConstraint.getDeleteAction() + "', but got '" + getForeignKeyAction(deleteRule) + "'", foreignKeyActionToInteger.get(matchingExpectedForeignKeyConstraint.getDeleteAction()), deleteRule);
                    }
                    if (matchingExpectedForeignKeyConstraint.getUpdateAction() != null) {
                        Assert.assertSame("Expected UPDATE action '" + matchingExpectedForeignKeyConstraint.getUpdateAction() + "', but got '" + getForeignKeyAction(updateRule) + "'", foreignKeyActionToInteger.get(matchingExpectedForeignKeyConstraint.getUpdateAction()), updateRule);
                    }
                    expectedForeignKeyConstraintSet.remove(matchingExpectedForeignKeyConstraint);
                }
                Assert.assertTrue("Not all expected foreign constraints could be confirmed: " + expectedForeignKeyConstraintSet, expectedForeignKeyConstraintSet.isEmpty());
            } catch(SQLException sqlException) {
                throw DatabaseExceptionBuilder.withCause(sqlException).build();
            }
        }
    }
    
}
