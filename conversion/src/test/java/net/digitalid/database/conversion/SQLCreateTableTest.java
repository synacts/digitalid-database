package net.digitalid.database.conversion;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

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
import net.digitalid.database.core.Site;
import net.digitalid.database.annotations.metadata.References;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestHost;

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class SQLCreateTableTest extends SQLTestBase {
    
    private final @Nonnull Site site = new TestHost();
    
    @Test
    public void shouldCreateTableWithoutColumns() throws Exception {
        final @Nonnull String tableName = "empty_table";
        // Takes name from table name, schema from site and columns from convertible classes.
        final @Nonnull TableImplementation table = SQL.create(tableName, site, EmptyClassConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
    }
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        final @Nonnull String tableName = "boolean_table_1";
        // Takes name from table name, schema from site and columns from convertible classes.
        TableImplementation table = SQL.create(tableName, site, SingleBooleanColumnTableConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));

        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[]{"boolean(1)"});
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        final @Nonnull String tableName = "boolean_table_2";
        // Takes name from table name, schema from site and columns from convertible classes.
        TableImplementation table = SQL.create(tableName, site, MultiBooleanColumnTableConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("firstvalue", new String[] { "boolean(1)" });
        expectedResult.put("secondvalue", new String[] { "boolean(1)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithBooleanColumnWithDefaultValue() throws Exception {
        final @Nonnull String tableName = "boolean_table_3";
        // Takes name from table name, schema from site and columns from convertible classes.
        TableImplementation table = SQL.create(tableName, site, BooleanColumnDefaultTrueTableConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[] { "boolean(1)", "NO", "", "TRUE" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithConstrainedInteger() throws Exception {
        final @Nonnull String tableName = "int_table_1";
        final @Nonnull TableImplementation table = SQL.create(tableName, site, ConstraintIntegerColumnTableConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        // TODO: check if integer(10) is really expected.
        expectedResult.put("value", new String[] { "integer(10)", "NO", "", "NULL", "((VALUE % 7) = 0)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithInheritance() throws Exception {
        final @Nonnull String tableName = "subclass_table_1";
        final @Nonnull TableImplementation table = SQL.create(tableName, site, SubClassConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("flag", new String[] { "boolean(1)" });
        expectedResult.put("number", new String[] { "integer(10)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    // TODO: fix
//    @Test
//    public void shouldCreateTableWithProperty() throws Exception {
//        final @Nonnull String tableName = "property_table_1";
//        final @Nonnull Table table = SQL.create(tableName, site, PropertyTableConverter.INSTANCE);
//        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
//        
//        assertTableExists(tableName, site.toString());
//        Map<String, String[]> expectedResult = new HashMap<>();
//        expectedResult.put("myproperty", new String[] { "boolean(1)" });
//        assertTableHasColumns(tableName, site.toString(), expectedResult);
//    }
    
    @Test
    public void shouldCreateTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull String tableName = "embedded_table_1";
        final @Nonnull TableImplementation table = SQL.create(tableName, site, EmbeddedConvertiblesConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value1", new String[] { "integer(10)" });
        expectedResult.put("value2", new String[] { "integer(10)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReference() throws Exception {
        final @Nonnull String tableName = "entity_table_1";
        final @Nonnull TableImplementation table = SQL.create(tableName, site, EntityConverter.INSTANCE);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));

        final @Nonnull String referencedTableName = Entity.class.getField("referencedEntity").getAnnotation(References.class).foreignTable();

        assertTableExists(referencedTableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("id", new String[]{"integer(10)"});
        expectedResult.put("othervalue", new String[]{"integer(10)"});
        assertTableHasColumns(referencedTableName, site.toString(), expectedResult);

        Map<String, String[]> expectedResult2 = new HashMap<>();
        expectedResult2.put("referencedentity", new String[]{"integer(10)"});
        assertTableHasColumns(tableName, site.toString(), expectedResult2);

        assertTableReferences(tableName, site.toString(), "referencedentity", "referenced_table_1", "id", UpdateAction.RESTRICT, DeleteAction.CASCADE);
    }
    
    @Test
    public void shouldCreateTableWithSimpleCollectionClass() throws Exception {
        final @Nonnull String collectionTableName = "collection_table_1";
        final @Nonnull TableImplementation collectionTable = SQL.create(collectionTableName, site, SimpleCollectionsClassConverter.INSTANCE);

        Assert.assertEquals(site.toString() + "." + collectionTableName, collectionTable.getName(site));
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("listofintegers", new String[]{"integer(10)"});
        expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
        assertTableHasColumns(collectionTableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReferencedCollectionClass() throws Exception {
        final @Nonnull String collectionTableName = "collection_table_2";
        final @Nonnull TableImplementation collectionTable = SQL.create(collectionTableName, site, ReferencedCollectionClassConverter.INSTANCE);

        Assert.assertEquals(site.toString() + "." + collectionTableName, collectionTable.getName(site));
        {
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            assertTableHasColumns(collectionTableName, site.toString(), expectedResult);
        }
        {
            assertTableExists("collection_table_2_listofintegers", site.toString());
    
            Map<String, String[]> expectedResult = new HashMap<>();
            expectedResult.put("additionalfield", new String[]{"integer(10)"});
            expectedResult.put("listofintegers", new String[]{"integer(10)"});
            expectedResult.put("_listofintegers_index_0", new String[]{"integer(10)"});
            assertTableHasColumns("collection_table_2_listofintegers", site.toString(), expectedResult);
        }
    }
    
}
