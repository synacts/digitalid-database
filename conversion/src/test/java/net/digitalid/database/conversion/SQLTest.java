package net.digitalid.database.conversion;

import java.sql.ResultSet;

import net.digitalid.database.conversion.h2.H2JDBCDatabaseInstance;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.jdbc.JDBCSelectionResult;

import net.digitalid.testing.base.TestBase;
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
public class SQLTest extends TestBase {
    
    private static Server server;
    
    @BeforeClass
    public static void setUpSQL() throws Exception {
        server = Server.createTcpServer();
        server.start();
        H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1;");
        //H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:mem:test");
        Database.initialize(h2Database);
    }
    
    @AfterClass
    public static void tearDownSQL() throws Exception {
        server.shutdown();
    }
    
    @Test
    public void shouldConnectToDatabase() throws Exception {
        DatabaseInstance instance = Database.getInstance();
        instance.execute("CREATE TABLE blubb");
        JDBCSelectionResult showTablesQuery = (JDBCSelectionResult) instance.executeSelect("SELECT table_name FROM information_schema.tables");
        ResultSet resultSet = showTablesQuery.getResultSet();
        if (resultSet.next()) {
            System.out.println("available tables:");
            System.out.println(resultSet.getString("table_name"));
        } else {
            System.out.println("no table created :_(");
        }
        SelectionResult tableExistsQuery = instance.executeSelect("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'BLUBB'");
        long count = tableExistsQuery.getInteger64();
        Assert.assertSame(1, count);
    }
    
    @Test
    public void shouldCreateTable() throws Exception {
        /*Site site = new TestHost();
        // Takes name from table name, schema from site and columns from convertible classes.
        SQL.create("tablename", site, Subclass.class, AnotherClass.class);*/
    }
}
