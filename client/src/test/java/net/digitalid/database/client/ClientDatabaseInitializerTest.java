package net.digitalid.database.client;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;

@TODO(task = "Rewrite.", date = "2017-03-05", author = Author.KASPAR_ETTER)
public class ClientDatabaseInitializerTest {
    
    private static final File FILE = new File(System.getProperty("user.home") + File.separator + ".DigitalID" + File.separator + "Data" + File.separator + "Test.db");
    
    @Pure
    private static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:sqlite:" + FILE.getAbsolutePath());
        connection.setAutoCommit(false);
        return connection;
    }
    
//    @Test
    @Pure
    public void testSomeMethod() throws SQLException, InterruptedException {
        new org.sqlite.JDBC();
        FILE.delete();
        
        final Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS test_identity (identity INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, category TINYINT NOT NULL, address VARCHAR(100) NOT NULL COLLATE BINARY)");
            connection.commit();
        }
        
        final Runnable runnable = new Runnable() {
            @Override
            @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
            public void run() {
                final long id = Thread.currentThread().getId();
                System.out.println("Start: " + id);
                
                try {
                    final Connection connection = getConnection();
                    try (Statement statement = connection.createStatement()) {
                        
                        try (ResultSet resultSet = statement.executeQuery("PRAGMA busy_timeout")) {
                            if (resultSet.next()) { System.out.println("PRAGMA: " + resultSet.getLong(1)); }
                            else { System.out.println("No PRAGMA."); }
                        }
                        
                        statement.executeUpdate("PRAGMA busy_timeout = 12000");
                        
                        System.out.println("Before 1: " + id);
                        statement.executeUpdate("INSERT INTO test_identity (category, address) VALUES (" + id + ", 'a@test.digitalid.net')");
                        System.out.println("After 1: " + id);
                        
                        Thread.sleep(2000);
                        
                        System.out.println("Before 2: " + id);
                        statement.executeUpdate("INSERT INTO test_identity (category, address) VALUES (" + id + ", 'b@test.digitalid.net')");
                        System.out.println("After 2: " + id);
                        
                        Thread.sleep(2000);
                        
                        System.out.println("Before 3: " + id);
                        statement.executeUpdate("INSERT INTO test_identity (category, address) VALUES (" + id + ", 'c@test.digitalid.net')");
                        System.out.println("After 3: " + id);
                        
                        connection.commit();
                    }
                } catch (InterruptedException | SQLException exception) {
                    System.out.println("Exception: " + id);
                    exception.printStackTrace();
                }
                
                System.out.println("End: " + id);
            }
        };
        
        final Thread thread1 = new Thread(runnable);
        thread1.start();
        
        final Thread thread2 = new Thread(runnable);
        thread2.start();
        
        final Thread thread3 = new Thread(runnable);
        thread3.start();
        
        thread1.join();
        thread2.join();
        thread3.join();
    }
    
}
