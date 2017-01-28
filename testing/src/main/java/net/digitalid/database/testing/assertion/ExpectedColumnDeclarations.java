package net.digitalid.database.testing.assertion;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;

import org.junit.Assert;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class ExpectedColumnDeclarations {
    
    private final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull ExpectedColumnDeclaration> expectedColumnDeclarations = FreezableHashMapBuilder.build();
    
    @Impure
    public void addExpectedResult(@Nonnull ExpectedColumnDeclaration expectedColumnDeclaration) {
        expectedColumnDeclarations.put(expectedColumnDeclaration.getColumnName(), expectedColumnDeclaration);
    }
    
    @Pure
    public void assertColumnFieldsExist(@Nonnull ResultSet resultSet) throws DatabaseException {
        try {
            while (resultSet.next()) {
                final @Nonnull String field = resultSet.getString("field");
                if (expectedColumnDeclarations.containsKey(field)) {
                    final @Nonnull ExpectedColumnDeclaration expectedColumnDeclaration = expectedColumnDeclarations.get(field);
                    expectedColumnDeclaration.found = true;
                    
                    final @Nonnull String type = resultSet.getString("type");
                    if (!expectedColumnDeclaration.getDBType().equals(type)) {
                        Assert.fail("The type " + type + " of column " + field + " was not expected (" + type + " != " + expectedColumnDeclaration.getDBType() + ")."); 
                    }
                    final @Nonnull String nullAllowed = resultSet.getString("null");
                    if (expectedColumnDeclaration.isNullAllowed() && !nullAllowed.equals("YES") || !expectedColumnDeclaration.isNullAllowed() && nullAllowed.equals("YES")) {
                        Assert.fail("The column " + field + " is " + (expectedColumnDeclaration.isNullAllowed() ? "" : "not ") + "expected to allow null values.");
                    }
                    final @Nonnull String key = resultSet.getString("key");
                    if (!expectedColumnDeclaration.getKey().equals(key)) {
                        Assert.fail("The key " + key + " of column " + field + " was not expected (" + key + " != " + expectedColumnDeclaration.getKey() + ")."); 
                    }
                    final @Nonnull String defaultValue = resultSet.getString("default");
                    if (!expectedColumnDeclaration.getDefaultValue().equals(defaultValue)) {
                        Assert.fail("The default value " + defaultValue + " of column " + field + " was not expected (" + defaultValue + " != " + expectedColumnDeclaration.getDefaultValue() + ").");
                    }
                } else {
                    Assert.fail("Unexpected column " + field + " found.");
                }
            }
            
            final @Nonnull StringBuilder stringBuilder = new StringBuilder();
            for (@Nonnull ExpectedColumnDeclaration expectedColumnDeclaration : expectedColumnDeclarations.values()) {
                if (!expectedColumnDeclaration.found) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(expectedColumnDeclaration.getColumnName());
                }
            }
            if (stringBuilder.length() > 0) {
                Assert.fail("The columns " + stringBuilder.toString() + " are not declared.");
            }
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
