package net.digitalid.database.conversion;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.conversion.testenvironment.columnconstraints.BooleanColumnDefaultTrueTableConverter;
import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
import net.digitalid.database.conversion.testenvironment.empty.EmptyClassConverter;
import net.digitalid.database.conversion.testenvironment.inherited.SubClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
import net.digitalid.database.conversion.testenvironment.referenced.Entity;
import net.digitalid.database.conversion.testenvironment.referenced.EntityConverter;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.interfaces.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.subject.site.SimpleSite;
import net.digitalid.database.testing.SQLTestBase;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class SQLCreateTableTest extends SQLTestBase {
    
    private static final @Nonnull SimpleSite site = SimpleSite.INSTANCE;
    
    @Impure
    @AfterClass
    public static void tearDown() throws FailedNonCommittingOperationException {
        dropTable(EmptyClassConverter.INSTANCE, site);
        dropTable(SingleBooleanColumnTableConverter.INSTANCE, site);
        dropTable(MultiBooleanColumnTableConverter.INSTANCE, site);
        dropTable(BooleanColumnDefaultTrueTableConverter.INSTANCE, site);
        dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, site);
        dropTable(SubClassConverter.INSTANCE, site);
        dropTable(EmbeddedConvertiblesConverter.INSTANCE, site);
        dropTable("referenced_table_1", site);
        dropTable(EntityConverter.INSTANCE, site);
        dropTable(SimpleCollectionsClassConverter.INSTANCE, site);
        dropTable(ReferencedCollectionClassConverter.INSTANCE.getTypeName()+ "_listofintegers", site);
        dropTable(ReferencedCollectionClassConverter.INSTANCE, site);
    }
    
    @Test
    public void shouldCreateTableWithoutColumns() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EmptyClassConverter.INSTANCE, site);
        Assert.assertEquals(EmptyClassConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(EmptyClassConverter.INSTANCE.getTypeName(), site.getSchemaName());
    }
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        TableImplementation table = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), table.getName());

        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[]{"boolean(1)" });
        assertTableHasColumns(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        TableImplementation table = SQL.create(MultiBooleanColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("firstvalue", new String[] { "boolean(1)" });
        expectedResult.put("secondvalue", new String[] { "boolean(1)" });
        assertTableHasColumns(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithBooleanColumnWithDefaultValue() throws Exception {
        TableImplementation table = SQL.create(BooleanColumnDefaultTrueTableConverter.INSTANCE, site);
        Assert.assertEquals(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[] { "boolean(1)", "NO", "", "TRUE" });
        assertTableHasColumns(BooleanColumnDefaultTrueTableConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithConstrainedInteger() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        // TODO: check if integer(10) is really expected.
        expectedResult.put("value", new String[] { "integer(10)", "NO", "", "NULL", "((VALUE % 7) = 0)" });
        assertTableHasColumns(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithInheritance() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(SubClassConverter.INSTANCE, site);
        Assert.assertEquals(SubClassConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(SubClassConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("flag", new String[] { "boolean(1)" });
        expectedResult.put("number", new String[] { "integer(10)" });
        assertTableHasColumns(SubClassConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    // TODO: fix
//    @Test
//    public void shouldCreateTableWithProperty() throws Exception {
//        final @Nonnull Table table = SQL.create(PropertyTableConverter.INSTANCE, site);
//        Assert.assertEquals(PropertyTableConverter.INSTANCE.getTypeName(), table.getName(site));
//        
//        assertTableExists(PropertyTableConverter.INSTANCE.getTypeName(), site.getSchemaName());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("myproperty", new String[] { "boolean(1)" });
//        assertTableHasColumns(PropertyTableConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
//    }
    
    @Test
    public void shouldCreateTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, site);
        Assert.assertEquals(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), table.getName());
        
        assertTableExists(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value1", new String[] { "integer(10)" });
        expectedResult.put("value2", new String[] { "integer(10)" });
        assertTableHasColumns(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReference() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EntityConverter.INSTANCE, site);
        Assert.assertEquals(EntityConverter.INSTANCE.getTypeName(), table.getName());
        
        final @Nonnull String referencedTableName = Entity.class.getField("referencedEntity").getAnnotation(ForeignKey.class).foreignTable();
        
        assertTableExists(referencedTableName, site.getSchemaName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("id", new String[]{"integer(10)"});
        expectedResult.put("othervalue", new String[]{"integer(10)"});
        assertTableHasColumns(referencedTableName, site.getSchemaName(), expectedResult);
        
        Map<String, String[]> expectedResult2 = new HashMap<>();
        expectedResult2.put("referencedentity", new String[]{"integer(10)"});
        assertTableHasColumns(EntityConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult2);
        
        assertTableReferences(EntityConverter.INSTANCE.getTypeName(), site.getSchemaName(), "referencedentity", "referenced_table_1", "id", UpdateAction.RESTRICT, DeleteAction.CASCADE);
    }
    
    @Test
    public void shouldCreateTableWithSimpleCollectionClass() throws Exception {
        final @Nonnull TableImplementation collectionTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, site);
        
        Assert.assertEquals(SimpleCollectionsClassConverter.INSTANCE.getTypeName(), collectionTable.getName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("listofintegers", new String[]{"integer(10)"});
        expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
        assertTableHasColumns(SimpleCollectionsClassConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReferencedCollectionClass() throws Exception {
        final @Nonnull TableImplementation collectionTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, site);

        Assert.assertEquals(ReferencedCollectionClassConverter.INSTANCE.getTypeName(), collectionTable.getName());
        {
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getTypeName(), site.getSchemaName(), expectedResult);
        }
        {
            assertTableExists(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", site.getSchemaName());
            
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            expectedResult.put("listofintegers", new String[]{"integer(10)"});
            expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", site.getSchemaName(), expectedResult);
        }
    }
    
}
