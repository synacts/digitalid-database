package net.digitalid.database.testing.h2;

import java.util.Properties;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.functional.iterables.FiniteIterable;
import net.digitalid.utility.tuples.Pair;

import net.digitalid.database.core.interfaces.SQLSelectionResult;
import net.digitalid.database.core.interfaces.SQLValueCollector;
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
    
    @Pure
    public static H2JDBCDatabaseInstance get(@Nonnull String url) {
        return new H2JDBCDatabaseInstance(url);
    }
    
    @Pure
    @Override
    protected @Nonnull String getURL() {
        return url;
    }
    
    @Pure
    @Override
    protected @Nonnull Properties getProperties() {
        final @Nonnull Properties properties = new Properties();
        properties.setProperty("user", "sa");
        properties.setProperty("password", "sa");
        return properties;
    }
    
    @Impure
    @Override
    public void dropDatabase() throws FailedOperationException {
        
    }
    
    @Impure
    @Override
    public void close() throws Exception {
        getConnection().close();
    }
    
    @Impure
    @Override
    public void execute(@Nonnull String sqlStatement) throws InternalException, FailedNonCommittingOperationException {
        executeUpdate(sqlStatement);
    }
    
    @Impure
    @Override
    public void execute(@Nonnull SQLValueCollector valueCollector) throws InternalException, FailedNonCommittingOperationException {
        executeBatch((JDBCValueCollector) valueCollector);
    }
    
    @Pure
    @Override
    public @Nonnull SQLValueCollector getValueCollector(@Nonnull FiniteIterable<@Nonnull String> preparedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, ReadOnlyList<@Nonnull Integer> columnCountForGroup) throws FailedNonCommittingOperationException {
        return JDBCValueCollector.get(preparedStatements.map(preparedStatement -> prepare(preparedStatement, false)), orderOfExecution, columnCountForGroup);
    }
    
    @Impure
    @Override
    public SQLSelectionResult executeSelect(@Nonnull SQLValueCollector valueCollector) throws InternalException, FailedNonCommittingOperationException {
        return executeSelect((JDBCValueCollector) valueCollector);
    }
}
