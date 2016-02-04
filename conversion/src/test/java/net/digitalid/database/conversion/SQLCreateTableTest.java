package net.digitalid.database.conversion;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.database.conversion.testenvironment.columnconstraints.BooleanColumnDefaultTrueTable;
import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.empty.EmptyClass;
import net.digitalid.database.conversion.testenvironment.inherited.SubClass;
import net.digitalid.database.conversion.testenvironment.property.PropertyTable;
import net.digitalid.database.conversion.testenvironment.referenced.Entity;
import net.digitalid.database.conversion.testenvironment.referenced.ReferencedEntity;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestHost;

/**
 */
public class SQLCreateTableTest extends SQLTestBase {
    
    private final @Nonnull Site site = new TestHost();
    
    @Test
    public void shouldCreateTableWithoutColumns() throws Exception {
        final @Nonnull String tableName = "empty_table";
        // Takes name from table name, schema from site and columns from convertible classes.
        final @Nonnull Table table = SQL.create(tableName, site, EmptyClass.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
    }
    
    @Test
    public void shouldCreateTableWithSingleBooleanColumn() throws Exception {
        final @Nonnull String tableName = "boolean_table_1";
        // Takes name from table name, schema from site and columns from convertible classes.
        Table table = SQL.create(tableName, site, SingleBooleanColumnTable.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[] { "boolean(1)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithMultipleBooleanColumns() throws Exception {
        final @Nonnull String tableName = "boolean_table_2";
        // Takes name from table name, schema from site and columns from convertible classes.
        Table table = SQL.create(tableName, site, MultiBooleanColumnTable.class);
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
        Table table = SQL.create(tableName, site, BooleanColumnDefaultTrueTable.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value", new String[] { "boolean(1)", "NO", "", "TRUE" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithConstrainedInteger() throws Exception {
        final @Nonnull String tableName = "int_table_1";
        final @Nonnull Table table = SQL.create(tableName, site, ConstraintIntegerColumnTable.class);
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
        final @Nonnull Table table = SQL.create(tableName, site, SubClass.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("flag", new String[] { "boolean(1)" });
        expectedResult.put("number", new String[] { "integer(10)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithProperty() throws Exception {
        final @Nonnull String tableName = "property_table_1";
        final @Nonnull Table table = SQL.create(tableName, site, PropertyTable.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("myproperty", new String[] { "boolean(1)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull String tableName = "embedded_table_1";
        final @Nonnull Table table = SQL.create(tableName, site, EmbeddedConvertibles.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        assertTableExists(tableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("value1", new String[] { "integer(10)" });
        expectedResult.put("value2", new String[] { "integer(10)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult);
    }
    
    @Test
    public void shouldCreateTableWithReference() throws Exception {
        final @Nonnull String referencedTableName = "referenced_table_1";
        final @Nonnull Table referencedTable = SQL.create(referencedTableName, site, ReferencedEntity.class);
        Assert.assertEquals(site.toString() + "." + referencedTableName, referencedTable.getName(site));
        
        assertTableExists(referencedTableName, site.toString());
        Map<String, String[]> expectedResult = new HashMap<>();
        expectedResult.put("id", new String[] { "integer(10)" });
        expectedResult.put("othervalue", new String[] { "integer(10)" });
        assertTableHasColumns(referencedTableName, site.toString(), expectedResult);
        
        final @Nonnull String tableName = "entity_table_1";
        final @Nonnull Table table = SQL.create(tableName, site, Entity.class);
        Assert.assertEquals(site.toString() + "." + tableName, table.getName(site));
        
        Map<String, String[]> expectedResult2 = new HashMap<>();
        expectedResult2.put("referencedentity", new String[] { "integer(10)" });
        assertTableHasColumns(tableName, site.toString(), expectedResult2);
        
        assertTableReferences(tableName, site.toString(), "referencedentity", "referenced_table_1", "id", UpdateAction.RESTRICT, DeleteAction.CASCADE);
    }
    
}
