package net.digitalid.database.conversion;

import org.h2.jdbc.JdbcSQLException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Nonnull;

import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.property.PropertyTable;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestHost;

/**
 *
 */
public class SQLInsertTableTest extends SQLTestBase {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static Table simpleBooleanTable;
    private static Table multiColumnBooleanTable;
    private static Table constraintIntegerColumnTable;
    private static Table propertyTable;
    private static Table convertibleTable;
    
    @BeforeClass
    public static void createTables() throws Exception {
        SQLTestBase.setUpSQL();
        Site site = new TestHost();
        simpleBooleanTable = SQL.create("boolean_table_1", site, SingleBooleanColumnTable.class);
        multiColumnBooleanTable = SQL.create("boolean_table_2", site, MultiBooleanColumnTable.class);
        constraintIntegerColumnTable = SQL.create("int_table_1", site, ConstraintIntegerColumnTable.class);
        propertyTable = SQL.create("property_table", site, PropertyTable.class);
        convertibleTable = SQL.create("embedded_table_1", site, EmbeddedConvertibles.class);
    }
    
    @Before
    public void deleteTableContent() throws FailedNonCommittingOperationException, EntryNotFoundException {
        DatabaseInstance instance = Database.getInstance();
        instance.execute("DELETE FROM " + simpleBooleanTable.getName().getValue().toUpperCase());
    }
    
    @Test
    public void shouldInsertIntoSimpleBooleanTable() throws Exception {
        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
        SQL.insert(convertibleObject, SingleBooleanColumnTable.class, simpleBooleanTable);
        
        assertRowCount(simpleBooleanTable, 1);
        assertTableContains(simpleBooleanTable, new String[][]{ {"value", "TRUE"} });
    }
    
    @Test
    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
        final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
        SQL.insert(convertibleObject, MultiBooleanColumnTable.class, multiColumnBooleanTable);
        
        assertRowCount(multiColumnBooleanTable, 1);
        assertTableContains(multiColumnBooleanTable, new String[][]{ {"firstValue", "TRUE"}, {"secondValue", "FALSE"} });
    }
    
    @Test
    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTable.class, constraintIntegerColumnTable);
        
        assertRowCount(constraintIntegerColumnTable, 1);
        assertTableContains(constraintIntegerColumnTable, new String[][]{ {"value", "14"} });
    }
    
    @Test
    public void shouldNotInsertIntoConstraintIntegerColumnTable() throws Exception {
        expectedException.expect(FailedUpdateExecutionException.class);
        expectedException.expectMessage("A database operation failed.");
        expectedException.expectCause(new BaseMatcher<Throwable>() {
            
            @Override
            public boolean matches(Object o) {
                if (o == null || !(o instanceof JdbcSQLException)) {
                    return false;
                }
                final @Nonnull JdbcSQLException exception = (JdbcSQLException) o;
                return exception.getMessage().startsWith("Check constraint violation:");
            }
    
            @Override
            public void describeTo(Description description) {
                description.appendText(JdbcSQLException.class.getName() + ": Check constraint violation: ...");
            }
            
        });
        
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(2);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTable.class, constraintIntegerColumnTable);
    }
    
    @Test
    public void shouldInsertIntoTableWithProperty() throws Exception {
        final @Nonnull PropertyTable convertible = PropertyTable.get();
        convertible.myProperty.set(true);
        SQL.insert(convertible, PropertyTable.class, propertyTable);
    
        assertRowCount(propertyTable, 1);
        assertTableContains(propertyTable, new String[][]{ {"myproperty", "TRUE"} });
    }
    
    @Test
    public void shouldInsertIntoTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull Convertible1 convertible1 = Convertible1.get(2);
        final @Nonnull Convertible2 convertible2 = Convertible2.get(3);
        final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertibles.get(convertible1, convertible2);
        SQL.insert(embeddedConvertibles, EmbeddedConvertibles.class, convertibleTable);
    
        assertRowCount(convertibleTable, 1);
        assertTableContains(convertibleTable, new String[][]{ {"value1", "2"}, {"value2", "3"} });
    }
    
}
