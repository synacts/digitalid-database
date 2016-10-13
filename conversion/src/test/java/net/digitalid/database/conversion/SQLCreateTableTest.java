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
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.interfaces.Site;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestSite;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class SQLCreateTableTest extends SQLTestBase {
    
    private static final @Nonnull Site site = new TestSite();
    
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
        dropTable(ReferencedCollectionClassConverter.INSTANCE.getName() + "_listofintegers", site);
        dropTable(ReferencedCollectionClassConverter.INSTANCE, site);
    }
    
    @Test
    public void shouldCreateTableWithoutColumns() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EmptyClassConverter.INSTANCE, site);
        Assert.assertEquals(EmptyClassConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(EmptyClassConverter.INSTANCE.getName(), site.toString());
    }
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        TableImplementation table = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(SingleBooleanColumnTableConverter.INSTANCE.getName(), table.getName());

        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[]{"boolean(1)" });
        assertTableHasColumns(SingleBooleanColumnTableConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        TableImplementation table = SQL.create(MultiBooleanColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(MultiBooleanColumnTableConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(MultiBooleanColumnTableConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("firstvalue", new String[] { "boolean(1)" });
        expectedResult.put("secondvalue", new String[] { "boolean(1)" });
        assertTableHasColumns(MultiBooleanColumnTableConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithBooleanColumnWithDefaultValue() throws Exception {
        TableImplementation table = SQL.create(BooleanColumnDefaultTrueTableConverter.INSTANCE, site);
        Assert.assertEquals(BooleanColumnDefaultTrueTableConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(BooleanColumnDefaultTrueTableConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[] { "boolean(1)", "NO", "", "TRUE" });
        assertTableHasColumns(BooleanColumnDefaultTrueTableConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithConstrainedInteger() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, site);
        Assert.assertEquals(ConstraintIntegerColumnTableConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(ConstraintIntegerColumnTableConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        // TODO: check if integer(10) is really expected.
        expectedResult.put("value", new String[] { "integer(10)", "NO", "", "NULL", "((VALUE % 7) = 0)" });
        assertTableHasColumns(ConstraintIntegerColumnTableConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithInheritance() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(SubClassConverter.INSTANCE, site);
        Assert.assertEquals(SubClassConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(SubClassConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("flag", new String[] { "boolean(1)" });
        expectedResult.put("number", new String[] { "integer(10)" });
        assertTableHasColumns(SubClassConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    // TODO: fix
//    @Test
//    public void shouldCreateTableWithProperty() throws Exception {
//        final @Nonnull Table table = SQL.create(PropertyTableConverter.INSTANCE, site);
//        Assert.assertEquals(PropertyTableConverter.INSTANCE.getName(), table.getName(site));
//        
//        assertTableExists(PropertyTableConverter.INSTANCE.getName(), site.toString());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("myproperty", new String[] { "boolean(1)" });
//        assertTableHasColumns(PropertyTableConverter.INSTANCE.getName(), site.toString(), expectedResult);
//    }
    
    @Test
    public void shouldCreateTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, site);
        Assert.assertEquals(EmbeddedConvertiblesConverter.INSTANCE.getName(), table.getName());
        
        assertTableExists(EmbeddedConvertiblesConverter.INSTANCE.getName(), site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value1", new String[] { "integer(10)" });
        expectedResult.put("value2", new String[] { "integer(10)" });
        assertTableHasColumns(EmbeddedConvertiblesConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReference() throws Exception {
        final @Nonnull TableImplementation table = SQL.create(EntityConverter.INSTANCE, site);
        Assert.assertEquals(EntityConverter.INSTANCE.getName(), table.getName());
        
        final @Nonnull String referencedTableName = Entity.class.getField("referencedEntity").getAnnotation(ForeignKey.class).foreignTable();
        
        assertTableExists(referencedTableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("id", new String[]{"integer(10)"});
        expectedResult.put("othervalue", new String[]{"integer(10)"});
        assertTableHasColumns(referencedTableName, site.toString(), expectedResult);

        Map<String, String[]> expectedResult2 = new HashMap<>();
        expectedResult2.put("referencedentity", new String[]{"integer(10)"});
        assertTableHasColumns(EntityConverter.INSTANCE.getName(), site.toString(), expectedResult2);
        
        assertTableReferences(EntityConverter.INSTANCE.getName(), site.toString(), "referencedentity", "referenced_table_1", "id", UpdateAction.RESTRICT, DeleteAction.CASCADE);
    }
    
    @Test
    public void shouldCreateTableWithSimpleCollectionClass() throws Exception {
        final @Nonnull TableImplementation collectionTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, site);
        
        Assert.assertEquals(SimpleCollectionsClassConverter.INSTANCE.getName(), collectionTable.getName());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("listofintegers", new String[]{"integer(10)"});
        expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
        assertTableHasColumns(SimpleCollectionsClassConverter.INSTANCE.getName(), site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReferencedCollectionClass() throws Exception {
        final @Nonnull TableImplementation collectionTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, site);

        Assert.assertEquals(ReferencedCollectionClassConverter.INSTANCE.getName(), collectionTable.getName());
        {
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getName(), site.toString(), expectedResult);
        }
        {
            assertTableExists(ReferencedCollectionClassConverter.INSTANCE.getName() + "_listofintegers", site.toString());
    
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            expectedResult.put("listofintegers", new String[]{"integer(10)"});
            expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
            assertTableHasColumns(ReferencedCollectionClassConverter.INSTANCE.getName() + "_listofintegers", site.toString(), expectedResult);
        }
    }
    
}
