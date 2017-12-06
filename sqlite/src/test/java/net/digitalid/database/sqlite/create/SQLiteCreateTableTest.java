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
package net.digitalid.database.sqlite.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclarationBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatementBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLReferenceBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLTypeBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLPrimaryKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.sqlite.SQLiteDialect;
import net.digitalid.database.sqlite.testbase.SQLiteDatabaseTest;

import org.junit.Test;

/**
 *
 */
public class SQLiteCreateTableTest extends SQLiteDatabaseTest {
    
    @Test
    public void shouldCreateSimpleTable() throws Exception {
        final @Nonnull String tableNameString = "Users1";
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(tableNameString).build();
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schemaName).build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("id").build()).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("name").build()).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(columnDeclarations);
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(immutableColumnDeclarations).build();
    
        final @Nonnull @SQLFraction String statement = SQLiteDialect.unparse(createTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", statement);
        getConnection().createStatement().execute(statement);
        getConnection().commit();
        assertTableExists(tableNameString);
    }
    
    @Test
    public void shouldCreateTableWithSinglePrimaryKey() throws Exception {
        final @Nonnull String tableNameString = "Users2";
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(tableNameString).build();
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schemaName).build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("id").build()).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withPrimaryKey(true).build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("name").build()).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(columnDeclarations);
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(immutableColumnDeclarations).build();
    
        final @Nonnull @SQLFraction String statement = SQLiteDialect.unparse(createTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", statement);
        getConnection().createStatement().execute(statement);
        getConnection().commit();
        assertTableExists(tableNameString);
    }
    
    @Test
    public void shouldCreateTableWithMultiplePrimaryKeys() throws Exception {
        final @Nonnull String tableNameString = "Users3";
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(tableNameString).build();
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schemaName).build();
        final @Nonnull SQLColumnName columnNameId = SQLColumnNameBuilder.withString("id").build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(columnNameId).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).build();
        final @Nonnull SQLColumnName columnNameName = SQLColumnNameBuilder.withString("name").build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(columnNameName).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(columnDeclarations);
        
        final @Nonnull FreezableArrayList<@Nonnull SQLTableConstraint> tableConstraints = FreezableArrayList.withElements(SQLPrimaryKeyConstraintBuilder.withColumns(ImmutableList.withElements(columnNameId, columnNameName)).build());
        
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(immutableColumnDeclarations).withTableConstraints(ImmutableList.withElementsOf(tableConstraints)).build();
    
        final @Nonnull @SQLFraction String statement = SQLiteDialect.unparse(createTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", statement);
        getConnection().createStatement().execute(statement);
        getConnection().commit();
        assertTableExists(tableNameString);
    }
    
    @Test
    public void shouldCreateTableWithSingleForeignKey() throws Exception {
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        
        final @Nonnull String usersTableString = "Users4";
        final @Nonnull SQLTableName usersTableName = SQLTableNameBuilder.withString(usersTableString).build();
        final @Nonnull SQLQualifiedTable usersTable = SQLExplicitlyQualifiedTableBuilder.withTable(usersTableName).withSchema(schemaName).build();
        final SQLColumnName idColumn = SQLColumnNameBuilder.withString("id").build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(idColumn).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withPrimaryKey(true).build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("name").build()).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> usersTableColumnDeclaration = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(usersTableColumnDeclaration);
        final @Nonnull SQLCreateTableStatement usersTableCreateTableStatement = SQLCreateTableStatementBuilder.withTable(usersTable).withColumnDeclarations(immutableColumnDeclarations).build();
    
        final @Nonnull @SQLFraction String usersTableCreateTableStatementAsString = SQLiteDialect.unparse(usersTableCreateTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", usersTableCreateTableStatementAsString);
        getConnection().createStatement().execute(usersTableCreateTableStatementAsString);
        getConnection().commit();
        assertTableExists(usersTableString);
        
        final @Nonnull String friendsTableString = "Friends1";
        final @Nonnull SQLTableName friendsTableName = SQLTableNameBuilder.withString(friendsTableString).build();
        final @Nonnull SQLQualifiedTable friendsTable = SQLExplicitlyQualifiedTableBuilder.withTable(friendsTableName).withSchema(schemaName).build();
        final @Nonnull SQLColumnDeclaration friendsId1ColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("id1").build()).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withReference(SQLReferenceBuilder.withTable(usersTable).withColumns(ImmutableList.withElements(idColumn)).build()).build();
        final @Nonnull SQLColumnDeclaration friendsId2ColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("id2").build()).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withReference(SQLReferenceBuilder.withTable(usersTable).withColumns(ImmutableList.withElements(idColumn)).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> friendsColumnDeclarations = FreezableArrayList.withElements(friendsId1ColumnDeclaration, friendsId2ColumnDeclaration);
        final @Nonnull SQLCreateTableStatement friendsCreateTableStatement = SQLCreateTableStatementBuilder.withTable(friendsTable).withColumnDeclarations(ImmutableList.withElementsOf(friendsColumnDeclarations)).build();
        
        final @Nonnull @SQLFraction String friendsReferencingStatement = SQLiteDialect.unparse(friendsCreateTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", friendsReferencingStatement);
        getConnection().createStatement().execute(friendsReferencingStatement);
        getConnection().commit();
        assertTableExists(friendsTableString);
    }
    
    @Test
    public void shouldCreateTableWithMultipleForeignKeys() throws Exception {
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        
        final @Nonnull String usersTableString = "Users5";
        final @Nonnull SQLTableName usersTableName = SQLTableNameBuilder.withString(usersTableString).build();
        final @Nonnull SQLQualifiedTable usersTable = SQLExplicitlyQualifiedTableBuilder.withTable(usersTableName).withSchema(schemaName).build();
        final SQLColumnName idColumn = SQLColumnNameBuilder.withString("id").build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(idColumn).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).withPrimaryKey(true).build();
        final SQLColumnName nameColumn = SQLColumnNameBuilder.withString("name").build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(nameColumn).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> usersTableColumnDeclaration = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(usersTableColumnDeclaration);
        final @Nonnull SQLCreateTableStatement usersTableCreateTableStatement = SQLCreateTableStatementBuilder.withTable(usersTable).withColumnDeclarations(immutableColumnDeclarations).build();
    
        final @Nonnull @SQLFraction String usersTableCreateTableStatementAsString = SQLiteDialect.unparse(usersTableCreateTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", usersTableCreateTableStatementAsString);
        getConnection().createStatement().execute(usersTableCreateTableStatementAsString);
        getConnection().commit();
        assertTableExists(usersTableString);
        
        final @Nonnull String friendsTableString = "Friends2";
        final @Nonnull SQLTableName friendsTableName = SQLTableNameBuilder.withString(friendsTableString).build();
        final @Nonnull SQLQualifiedTable friendsTable = SQLExplicitlyQualifiedTableBuilder.withTable(friendsTableName).withSchema(schemaName).build();
        final SQLColumnName id1Column = SQLColumnNameBuilder.withString("id1").build();
        final @Nonnull SQLColumnDeclaration friendsId1ColumnDeclaration = SQLColumnDeclarationBuilder.withName(id1Column).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).build();
        final SQLColumnName name1Column = SQLColumnNameBuilder.withString("name1").build();
        final @Nonnull SQLColumnDeclaration friendsName1ColumnDeclaration = SQLColumnDeclarationBuilder.withName(name1Column).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> friendsColumnDeclarations = FreezableArrayList.withElements(friendsId1ColumnDeclaration, friendsName1ColumnDeclaration);
        
        final @Nonnull FreezableArrayList<@Nonnull SQLTableConstraint> tableConstraints = FreezableArrayList.withElements(SQLForeignKeyConstraintBuilder.<SQLColumnName>withColumns(ImmutableList.withElements(id1Column, name1Column)).withReference(SQLReferenceBuilder.withTable(usersTable).withColumns(ImmutableList.withElements(idColumn, nameColumn)).build()).build());
        
        final @Nonnull SQLCreateTableStatement friendsCreateTableStatement = SQLCreateTableStatementBuilder.withTable(friendsTable).withColumnDeclarations(ImmutableList.withElementsOf(friendsColumnDeclarations)).withTableConstraints(ImmutableList.withElementsOf(tableConstraints)).build();
        
        final @Nonnull @SQLFraction String friendsReferencingStatement = SQLiteDialect.unparse(friendsCreateTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", friendsReferencingStatement);
        getConnection().createStatement().execute(friendsReferencingStatement);
        getConnection().commit();
        assertTableExists(friendsTableString);
    }
    
    @Test
    public void shouldCreateTableWithDefaultValues() throws Exception {
        final @Nonnull String tableNameString = "Users6";
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(tableNameString).build();
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(SCHEMA_NAME).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schemaName).build();
        final @Nonnull SQLColumnDeclaration idColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("id").build()).withType(SQLTypeBuilder.withType(CustomType.INTEGER32).build()).build();
        final @Nonnull SQLColumnDeclaration nameColumnDeclaration = SQLColumnDeclarationBuilder.withName(SQLColumnNameBuilder.withString("name").build()).withType(SQLTypeBuilder.withType(CustomType.STRING64).build()).withDefaultValue(SQLStringLiteralBuilder.withString("unnamed").build()).build();
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withElements(idColumnDeclaration, nameColumnDeclaration);
        final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> immutableColumnDeclarations = ImmutableList.withElementsOf(columnDeclarations);
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatementBuilder.withTable(qualifiedTable).withColumnDeclarations(immutableColumnDeclarations).build();
    
        final @Nonnull @SQLFraction String statement = SQLiteDialect.unparse(createTableStatement, Unit.DEFAULT);
        Log.verbose("Statement: $", statement);
        getConnection().createStatement().execute(statement);
        getConnection().commit();
        assertTableExists(tableNameString);
    }
    
}
