package net.digitalid.database.testing.h2;

import java.util.Properties;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedOperationException;
import net.digitalid.database.jdbc.JDBCDatabaseInstance;
import net.digitalid.database.jdbc.JDBCValueCollector;

import org.h2.Driver;

/**
 *
 */
public class H2JDBCDatabaseInstance extends JDBCDatabaseInstance {
    
    final @Nonnull String url;
    
    private H2JDBCDatabaseInstance(@Nonnull String url) {
        super(new Driver());
        this.url = url;
    }
    
    public static H2JDBCDatabaseInstance get(@Nonnull String url) {
        return new H2JDBCDatabaseInstance(url);
    }
    
    @Override
    protected @Nonnull String getURL() {
        return url;
    }
    
    @Override
    protected @Nonnull Properties getProperties() {
        final @Nonnull Properties properties = new Properties();
        properties.setProperty("user", "sa");
        properties.setProperty("password", "");
        return properties;
    }
    
    @Override
    public void dropDatabase() throws FailedOperationException {
        
    }
    
    @Override
    public void close() throws Exception {
        getConnection().close();
    }
    
    @Override
    public void execute(@Nonnull String sqlStatement) throws InternalException, FailedNonCommittingOperationException {
        executeUpdate(sqlStatement);
    }
    
    @Override
    public void execute(@Nonnull ValueCollector valueCollector) throws InternalException, FailedNonCommittingOperationException {
        executeUpdate((JDBCValueCollector) valueCollector);
    }
    
    @Override
    public @Nonnull ValueCollector getValueCollector(@Nonnull String preparedStatement) throws FailedNonCommittingOperationException {
        return JDBCValueCollector.get(prepare(preparedStatement, false));
    }
    
    @Override
    public SelectionResult executeSelect(@Nonnull ValueCollector valueCollector) throws InternalException, FailedNonCommittingOperationException {
        return executeSelect((JDBCValueCollector) valueCollector);
    }
}
