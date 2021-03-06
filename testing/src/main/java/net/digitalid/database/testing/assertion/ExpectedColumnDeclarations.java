/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.testing.assertion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.Database;

import org.junit.Assert;

@GenerateBuilder
@GenerateSubclass
public abstract class ExpectedColumnDeclarations {
    
    private final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull ExpectedColumnDeclaration> expectedColumnDeclarations = FreezableHashMapBuilder.build();
    
    @Impure
    public void addExpectedResult(@Nonnull ExpectedColumnDeclaration expectedColumnDeclaration) {
        expectedColumnDeclarations.put(expectedColumnDeclaration.getColumnName(), expectedColumnDeclaration);
    }
    
    @Pure
    public void assertColumnFieldsExist(@Nonnull String tableName, @Nonnull Database database) throws DatabaseException {
        final @Nonnull String query = "SHOW COLUMNS FROM " + tableName;
        final @Nonnull ResultSet resultSet = database.executeQuery(query);
    
        try {
            while (resultSet.next()) {
                final @Nonnull String field = resultSet.getString("field");
                if (expectedColumnDeclarations.containsKey(field)) {
                    final @Nonnull ExpectedColumnDeclaration expectedColumnDeclaration = expectedColumnDeclarations.get(field);
                    expectedColumnDeclaration.setFound(true);
                    
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
                if (!expectedColumnDeclaration.isFound()) {
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
    
    /**
     * Checks whether the expected column constraints exist in the Database definition.
     */
    @Pure
    public void assertColumnConstraintsExist(@Nonnull String tableName, @Nonnull Database database) throws DatabaseException {
        for (Map.Entry<@Nonnull String, @Nonnull ExpectedColumnDeclaration> entry : expectedColumnDeclarations.entrySet()) {
            if (entry.getValue().getColumnConstraint() != null) {
                final @Nonnull String columnName = entry.getKey();
                final @Nonnull String columnConstraint = entry.getValue().getColumnConstraint();
                final @Nonnull String constraintQuery = "SELECT CHECK_CONSTRAINT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + columnName + "'";
                final @Nonnull ResultSet resultSet = database.executeQuery(constraintQuery);
                try {
                    resultSet.next();
                    final @Nonnull String actualColumnConstraint = resultSet.getString(1);
                    Assert.assertEquals(columnConstraint, actualColumnConstraint);
                } catch (SQLException e) {
                    throw DatabaseExceptionBuilder.withCause(e).build();
                }
            }
        }
    }
}
