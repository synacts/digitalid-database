package net.digitalid.database.conversion;

import net.digitalid.database.conversion.h2.H2JDBCDatabaseInstance;
import net.digitalid.database.conversion.testenvironment.AnotherClass;
import net.digitalid.database.conversion.testenvironment.SubClass;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.utility.testing.TestingBase;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * SQLTestBase establishes a connection to a database and provides assert statements for checking
 * whether DDL and DML statements where successful.
 */
public class SQLTest extends TestingBase {
    
    private static Server server;
    
    @BeforeClass
    public static void setUpSQL() throws Exception {
        server = Server.createTcpServer();
        server.start();
        //H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1;");
        H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:mem:test");
        Database.initialize(h2Database);
    }
    
    @AfterClass
    public static void tearDownSQL() throws Exception {
        server.shutdown();
    }
    
    /**
     * A generic test that verifies that a connection to the database could be established, and that a table could be created by issuing an SQL statement.
     */
    @Test
    public void shouldConnectToDatabase() throws Exception {
        DatabaseInstance instance = Database.getInstance();
        instance.execute("CREATE TABLE blubb");
        // TODO: implement h2 with transcriber
        SelectionResult tableExistsQuery = instance.executeSelect("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'BLUBB'");
        tableExistsQuery.moveToFirstRow();
        Assert.assertSame(1, tableExistsQuery.getInteger32());
    }
    
    @Test
    public void shouldCreateTable() throws Exception {
        Site site = new TestHost();
        // Takes name from table name, schema from site and columns from convertible classes.
        Table table = SQL.create("tablename", site, SubClass.class, AnotherClass.class);
        Assert.assertEquals(site.toString() + ".tablename", table.getName(site));
        
        DatabaseInstance instance = Database.getInstance();
        SelectionResult tableExistsQuery = instance.executeSelect("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'TABLENAME' and table_schema = '" + site.toString() + "'");
        tableExistsQuery.moveToFirstRow();
        Assert.assertSame(1, tableExistsQuery.getInteger32());
    }
    
}
