package net.digitalid.database.conversion;

import java.util.Set;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.set.FreezableHashSet;
import net.digitalid.utility.storage.enumerations.ForeignKeyAction;
import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.conversion.testenvironment.columnconstraints.BooleanColumnDefaultTrueTableConverter;
import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.inherited.SubClassConverter;
import net.digitalid.database.conversion.testenvironment.referenced.EntityConverter;
import net.digitalid.database.conversion.testenvironment.referenced.ReferencedEntityConverter;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.DatabaseTest;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarationBuilder;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarations;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarationsBuilder;
import net.digitalid.database.testing.assertion.ExpectedForeignKeyConstraint;
import net.digitalid.database.testing.assertion.ExpectedForeignKeyConstraintBuilder;
import net.digitalid.database.testing.assertion.ExpectedTableConstraint;
import net.digitalid.database.testing.assertion.ExpectedTableConstraintBuilder;
import net.digitalid.database.testing.assertion.ExpectedTableConstraints;
import net.digitalid.database.testing.assertion.ExpectedTableConstraintsBuilder;

import org.junit.Test;

public class SQLCreateTableTest extends DatabaseTest {
    
    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("value").withDBType("boolean(1)").withNullAllowed(false).withKey("PRI").build());
        assertTableHasExpectedColumnsDeclaration(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }
    
    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        SQL.createTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
        
        assertTableExists(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
        
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("firstvalue").withDBType("boolean(1)").withNullAllowed(false).withKey("PRI").build());
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("secondvalue").withDBType("boolean(1)").withNullAllowed(false).withKey("PRI").build());
        assertTableHasExpectedColumnsDeclaration(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }
    
    @Test
    public void shouldCreateTableWithBooleanColumnWithDefaultValue() throws Exception {
        SQL.createTable(BooleanColumnDefaultTrueTableConverter.INSTANCE, unit);
        
        assertTableExists(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), unit.getName());
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("value").withDBType("boolean(1)").withNullAllowed(false).withDefaultValue("TRUE").withKey("PRI").build());
        assertTableHasExpectedColumnsDeclaration(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }

    @Test
    public void shouldCreateTableWithConstrainedInteger() throws Exception {
        SQL.createTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        
        assertTableExists(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("value").withDBType("integer(10)").withNullAllowed(false).withColumnConstraint("((VALUE % 7) = 0)").withKey("PRI").build());
        assertTableHasExpectedColumnsDeclaration(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }
    
    @Test
    public void shouldCreateTableWithInheritance() throws Exception {
        SQL.createTable(SubClassConverter.INSTANCE, unit);
        
        assertTableExists(SubClassConverter.INSTANCE.getTypeName(), unit.getName());
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("flag").withDBType("boolean(1)").withNullAllowed(false).withKey("PRI").build());
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("number").withDBType("integer(10)").withNullAllowed(false).withKey("PRI").build());
        assertTableHasExpectedColumnsDeclaration(SubClassConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }
    
    // TODO: fix or move to property module
//    @Test
//    public void shouldCreateTableWithProperty() throws Exception {
//        final @Nonnull Table table = SQL.create(PropertyTableConverter.INSTANCE, site);
//        Assert.assertEquals(PropertyTableConverter.INSTANCE.getTypeName(), table.getName(site));
//        
//        assertTableExists(PropertyTableConverter.INSTANCE.getTypeName(), site.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("myproperty", new String[] { "boolean(1)" });
//        assertTableHasColumns(PropertyTableConverter.INSTANCE.getTypeName(), site.getName(), expectedResult);
//    }
    
    @Test
    public void shouldCreateTableWithReference() throws Exception {
        SQL.createTable(ReferencedEntityConverter.INSTANCE, unit);
        SQL.createTable(EntityConverter.INSTANCE, unit);
        
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarationsForEntityTable = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarationsForEntityTable.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("referencedentity_id").withDBType("integer(10)").withNullAllowed(false).withKey("PRI").build());
        expectedColumnDeclarationsForEntityTable.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("referencedentity_othervalue").withDBType("integer(10)").withNullAllowed(false).withKey("PRI").build());
        
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarationsForReferencedTable = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarationsForReferencedTable.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("id").withDBType("integer(10)").withNullAllowed(false).withKey("PRI").build());
        expectedColumnDeclarationsForReferencedTable.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("othervalue").withDBType("integer(10)").withNullAllowed(false).withKey("PRI").build());
        
        assertTableHasExpectedColumnsDeclaration(ReferencedEntityConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarationsForReferencedTable);
        assertTableHasExpectedColumnsDeclaration(EntityConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarationsForEntityTable);
        
        final @Nonnull ExpectedTableConstraints expectedTableConstraints = ExpectedTableConstraintsBuilder.build();
        final @Nonnull ExpectedForeignKeyConstraint expectedForeignKeyConstraintOnId = ExpectedForeignKeyConstraintBuilder.withForeignKeyColumn("referencedentity_id").withReferencedTable(ReferencedEntityConverter.INSTANCE.getTypeName()).withReferencedColumn("id").withDeleteAction(ForeignKeyAction.CASCADE).withUpdateAction(ForeignKeyAction.RESTRICT).build();
        final @Nonnull ExpectedForeignKeyConstraint expectedForeignKeyConstraintOnOtherValue = ExpectedForeignKeyConstraintBuilder.withForeignKeyColumn("referencedentity_othervalue").withReferencedTable(ReferencedEntityConverter.INSTANCE.getTypeName()).withReferencedColumn("othervalue").withDeleteAction(ForeignKeyAction.CASCADE).withUpdateAction(ForeignKeyAction.RESTRICT).build();
        
        final @Nonnull Set<ExpectedForeignKeyConstraint> foreignKeyConstraints = FreezableHashSet.withElements(expectedForeignKeyConstraintOnId, expectedForeignKeyConstraintOnOtherValue);
        final @Nonnull ExpectedTableConstraint expectedTableConstraint = ExpectedTableConstraintBuilder.withTableName(EntityConverter.INSTANCE.getTypeName()).withSchema(unit.getName()).withForeignKeyConstraints(foreignKeyConstraints).build();
        expectedTableConstraints.addExpectedResult(expectedTableConstraint);
        assertTableReferences(expectedTableConstraints);
    }
    
}
