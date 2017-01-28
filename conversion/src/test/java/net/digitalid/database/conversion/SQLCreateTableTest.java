package net.digitalid.database.conversion;

// TODO

import javax.annotation.Nonnull;

import net.digitalid.database.conversion.testenvironment.columnconstraints.BooleanColumnDefaultTrueTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarationBuilder;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarations;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarationsBuilder;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Nonnull;
//
//import net.digitalid.utility.annotations.method.Impure;
//
//import net.digitalid.database.annotations.constraints.ForeignKey;
//import net.digitalid.database.conversion.testenvironment.columnconstraints.BooleanColumnDefaultTrueTableConverter;
//import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
//import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
//import net.digitalid.database.conversion.testenvironment.empty.EmptyClassConverter;
//import net.digitalid.database.conversion.testenvironment.inherited.SubClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
//import net.digitalid.database.conversion.testenvironment.referenced.Entity;
//import net.digitalid.database.conversion.testenvironment.referenced.EntityConverter;
//import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
//import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
//import net.digitalid.database.exceptions.DatabaseException;
//import net.digitalid.database.testing.SQLTestBase;
//import net.digitalid.database.unit.Unit;
//
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.Test;
//
public class SQLCreateTableTest extends SQLTestBase {
    
    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
//    @Impure
//    @AfterClass
//    public static void tearDown() throws DatabaseException {
//        dropTable(EmptyClassConverter.INSTANCE, unit);
//        dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
//        dropTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
//        dropTable(BooleanColumnDefaultTrueTableConverter.INSTANCE, unit);
//        dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        dropTable(SubClassConverter.INSTANCE, unit);
//        dropTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
//        dropTable("referenced_table_1", unit);
//        dropTable(EntityConverter.INSTANCE, unit);
//        dropTable(SimpleCollectionsClassConverter.INSTANCE, unit);
//        dropTable(ReferencedCollectionClassConverter.INSTANCE.getTypeName()+ "_listofintegers", unit);
//        dropTable(ReferencedCollectionClassConverter.INSTANCE, unit);
//    }
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("value").withDBType("boolean(1)").build());
        assertTableHasColumns(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }

    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        SQL.createTable(MultiBooleanColumnTableConverter.INSTANCE, unit);

        assertTableExists(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    
        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("firstvalue").withDBType("boolean(1)").build());
        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("secondvalue").withDBType("boolean(1)").build());
        assertTableHasColumns(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
    }

//    @Test
//    public void shouldCreateTableWithBooleanColumnWithDefaultValue() throws Exception {
//        SQL.createTable(BooleanColumnDefaultTrueTableConverter.INSTANCE, unit);
//
//        assertTableExists(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), unit.getName());
//        final @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations = ExpectedColumnDeclarationsBuilder.build();
//        expectedColumnDeclarations.addExpectedResult(ExpectedColumnDeclarationBuilder.withColumnName("value").withDBType("boolean(1)").withNullAllowed(false).withDefaultValue("TRUE").build());
//        assertTableHasColumns(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedColumnDeclarations);
//    }

//    @Test
//    public void shouldCreateTableWithConstrainedInteger() throws Exception {
//        final @Nonnull TableImplementation table = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        Assert.assertEquals(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), table.getName());
//        
//        assertTableExists(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        // TODO: check if integer(10) is really expected.
//        expectedResult.put("value", new String[] { "integer(10)", "NO", "", "NULL", "((VALUE % 7) = 0)" });
//        assertTableHasColumns(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult);
//    }
//    
//    @Test
//    public void shouldCreateTableWithInheritance() throws Exception {
//        final @Nonnull TableImplementation table = SQL.create(SubClassConverter.INSTANCE, unit);
//        Assert.assertEquals(SubClassConverter.INSTANCE.getTypeName(), table.getName());
//        
//        assertTableExists(SubClassConverter.INSTANCE.getTypeName(), unit.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("flag", new String[] { "boolean(1)" });
//        expectedResult.put("number", new String[] { "integer(10)" });
//        assertTableHasColumns(SubClassConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult);
//    }
//    
//    // TODO: fix
////    @Test
////    public void shouldCreateTableWithProperty() throws Exception {
////        final @Nonnull Table table = SQL.create(PropertyTableConverter.INSTANCE, site);
////        Assert.assertEquals(PropertyTableConverter.INSTANCE.getTypeName(), table.getName(site));
////        
////        assertTableExists(PropertyTableConverter.INSTANCE.getTypeName(), site.getName());
////        Map<String, String[]> expectedResult = new HashMap<>();
////        expectedResult.put("myproperty", new String[] { "boolean(1)" });
////        assertTableHasColumns(PropertyTableConverter.INSTANCE.getTypeName(), site.getName(), expectedResult);
////    }
//    
//    @Test
//    public void shouldCreateTableWithEmbeddedConvertibles() throws Exception {
//        final @Nonnull TableImplementation table = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, unit);
//        Assert.assertEquals(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), table.getName());
//        
//        assertTableExists(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("value1", new String[] { "integer(10)" });
//        expectedResult.put("value2", new String[] { "integer(10)" });
//        assertTableHasColumns(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult);
//    }
//    
//    @Test
//    public void shouldCreateTableWithReference() throws Exception {
//        final @Nonnull TableImplementation table = SQL.create(EntityConverter.INSTANCE, unit);
//        Assert.assertEquals(EntityConverter.INSTANCE.getTypeName(), table.getName());
//        
//        final @Nonnull String referencedTableName = Entity.class.getField("referencedEntity").getAnnotation(ForeignKey.class).foreignTable();
//        
//        assertTableExists(referencedTableName, unit.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("id", new String[]{"integer(10)"});
//        expectedResult.put("othervalue", new String[]{"integer(10)"});
//        assertTableHasColumns(referencedTableName, unit.getName(), expectedResult);
//        
//        Map<String, String[]> expectedResult2 = new HashMap<>();
//        expectedResult2.put("referencedentity", new String[]{"integer(10)"});
//        assertTableHasColumns(EntityConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult2);
//        
//        assertTableReferences(EntityConverter.INSTANCE.getTypeName(), unit.getName(), "referencedentity", "referenced_table_1", "id", UpdateAction.RESTRICT, DeleteAction.CASCADE);
//    }
//    
//    @Test
//    public void shouldCreateTableWithSimpleCollectionClass() throws Exception {
//        final @Nonnull TableImplementation collectionTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, unit);
//        
//        Assert.assertEquals(SimpleCollectionsClassConverter.INSTANCE.getTypeName(), collectionTable.getName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("listofintegers", new String[]{"integer(10)"});
//        expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
//        assertTableHasColumns(SimpleCollectionsClassConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult);
//    }
//    
//    @Test
//    public void shouldCreateTableWithReferencedCollectionClass() throws Exception {
//        final @Nonnull TableImplementation collectionTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, unit);
//
//        Assert.assertEquals(ReferencedCollectionClassConverter.INSTANCE.getTypeName(), collectionTable.getName());
//        {
//            Map<String, String[]> expectedResult = new HashMap<>();
//            expectedResult.put("additionalfield", new String[]{"integer(10)"});
//            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getTypeName(), unit.getName(), expectedResult);
//        }
//        {
//            assertTableExists(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", unit.getName());
//            
//            Map<String, String[]> expectedResult = new HashMap<>();
//            expectedResult.put("additionalfield", new String[]{"integer(10)"});
//            expectedResult.put("listofintegers", new String[]{"integer(10)"});
//            expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
//            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", unit.getName(), expectedResult);
//        }
//    }
//    
}
