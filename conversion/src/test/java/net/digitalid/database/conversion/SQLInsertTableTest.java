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

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClass;
import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClass;
import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClass;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClass;
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
    private static Table simpleCollectionsTable;
    private static Table collectionAndAdditionalFieldTable;
    private static Table compositeCollectionTable;
    private static Table referencedCollectionFieldTable;
    
    @BeforeClass
    public static void createTables() throws Exception {
        SQLTestBase.setUpSQL();
        Site site = new TestHost();
        simpleBooleanTable = SQL.create("SQLInsertTableTest_boolean_table_1", site, SingleBooleanColumnTable.class);
        multiColumnBooleanTable = SQL.create("SQLInsertTableTest_boolean_table_2", site, MultiBooleanColumnTable.class);
        constraintIntegerColumnTable = SQL.create("SQLInsertTableTest_int_table_1", site, ConstraintIntegerColumnTable.class);
        propertyTable = SQL.create("SQLInsertTableTest_property_table", site, PropertyTable.class);
        convertibleTable = SQL.create("SQLInsertTableTest_embedded_table_1", site, EmbeddedConvertibles.class);
        simpleCollectionsTable = SQL.create("SQLInsertTableTest_collections_table_1", site, SimpleCollectionsClass.class);
        collectionAndAdditionalFieldTable = SQL.create("SQLInsertTableTest_collections_table_2", site, CollectionAndAdditionalFieldClass.class);
        compositeCollectionTable = SQL.create("SQLInsertTableTest_collections_table_3", site, CompositeCollectionsClass.class);
        referencedCollectionFieldTable = SQL.create("SQLInsertTableTest_collections_table_4", site, ReferencedCollectionClass.class);
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
        assertTableContains(simpleBooleanTable, Expected.column("value").value("TRUE"));
    }
    
    @Test
    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
        final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
        SQL.insert(convertibleObject, MultiBooleanColumnTable.class, multiColumnBooleanTable);
        
        assertRowCount(multiColumnBooleanTable, 1);
        assertTableContains(multiColumnBooleanTable, Expected.column("firstvalue").value("TRUE"), Expected.column("secondValue").value("FALSE"));
    }
    
    @Test
    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTable.class, constraintIntegerColumnTable);
        
        assertRowCount(constraintIntegerColumnTable, 1);
        assertTableContains(constraintIntegerColumnTable, Expected.column("value").value("14"));
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
        assertTableContains(propertyTable, Expected.column("myproperty").value("TRUE"));
    }
    
    @Test
    public void shouldInsertIntoTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull Convertible1 convertible1 = Convertible1.get(2);
        final @Nonnull Convertible2 convertible2 = Convertible2.get(3);
        final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertibles.get(convertible1, convertible2);
        SQL.insert(embeddedConvertibles, EmbeddedConvertibles.class, convertibleTable);
    
        assertRowCount(convertibleTable, 1);
        assertTableContains(convertibleTable, Expected.column("value1").value("2"), Expected.column("value2").value("3"));
    }
    
    @Test
    public void shouldInsertIntoTableWithSimpleCollectionsClass() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.get();
        listOfIntegers.add(1);
        listOfIntegers.add(2);
        listOfIntegers.add(3);
        listOfIntegers.add(4);
        listOfIntegers.add(5);
        final @Nonnull SimpleCollectionsClass simpleCollectionsClass = SimpleCollectionsClass.get(listOfIntegers);
        SQL.insert(simpleCollectionsClass, SimpleCollectionsClass.class, simpleCollectionsTable);
    
        assertRowCount(simpleCollectionsTable, 5L);
        assertTableContains(simpleCollectionsTable, 
                Expected.column("listofintegers").value("1"),
                Expected.column("listofintegers").value("2"),
                Expected.column("listofintegers").value("3"),
                Expected.column("listofintegers").value("4"),
                Expected.column("listofintegers").value("5"));
    }
    
    @Test
    public void shouldInsertIntoTableWithCollectionAndAdditionalFieldClass() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.get();
        listOfIntegers.add(1);
        listOfIntegers.add(2);
        listOfIntegers.add(3);
        listOfIntegers.add(4);
        listOfIntegers.add(5);
        final @Nonnull CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = CollectionAndAdditionalFieldClass.get(99, listOfIntegers);
        SQL.insert(collectionAndAdditionalFieldClass, CollectionAndAdditionalFieldClass.class, collectionAndAdditionalFieldTable);
    
        assertRowCount(collectionAndAdditionalFieldTable, 5L);
        assertTableContains(collectionAndAdditionalFieldTable,
                Expected.column("listofintegers").value("1").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("2").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("3").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("4").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("5").and("additionalfield").value("99")
                );
    }
    
    @Test
    public void shouldInsertIntoTableWithCompositeCollection() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers1 = FreezableArrayList.get();
        listOfIntegers1.add(1);
        listOfIntegers1.add(2);
        listOfIntegers1.add(3);
        listOfIntegers1.add(4);
        listOfIntegers1.add(5);
        
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers2 = FreezableArrayList.get();
        listOfIntegers2.add(100);
        listOfIntegers2.add(200);
        listOfIntegers2.add(300);
        listOfIntegers2.add(400);
        
        final @Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers = FreezableArrayList.get();
        listOfListOfIntegers.add(listOfIntegers1);
        listOfListOfIntegers.add(listOfIntegers2);
        
        final @Nonnull CompositeCollectionsClass collectionAndAdditionalFieldClass = CompositeCollectionsClass.get(listOfListOfIntegers);
        SQL.insert(collectionAndAdditionalFieldClass, CompositeCollectionsClass.class, compositeCollectionTable);
        
        assertRowCount(compositeCollectionTable, 9L);
        assertTableContains(compositeCollectionTable,
                Expected.column("_listoflistofintegers_index").value("0").and("listoflistofintegers").value("1"),
                Expected.column("_listoflistofintegers_index").value("0").and("listoflistofintegers").value("2"),
                Expected.column("_listoflistofintegers_index").value("0").and("listoflistofintegers").value("3"),
                Expected.column("_listoflistofintegers_index").value("0").and("listoflistofintegers").value("4"),
                Expected.column("_listoflistofintegers_index").value("0").and("listoflistofintegers").value("5"),
                Expected.column("_listoflistofintegers_index").value("1").and("listoflistofintegers").value("100"),
                Expected.column("_listoflistofintegers_index").value("1").and("listoflistofintegers").value("200"),
                Expected.column("_listoflistofintegers_index").value("1").and("listoflistofintegers").value("300"),
                Expected.column("_listoflistofintegers_index").value("1").and("listoflistofintegers").value("400")
        );
    }
    
    @Test
    public void shouldInsertIntoTableWithReferencedCollection() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.get();
        listOfIntegers.add(1);
        listOfIntegers.add(2);
        listOfIntegers.add(3);
        listOfIntegers.add(4);
        listOfIntegers.add(5);
        final @Nonnull ReferencedCollectionClass collectionAndAdditionalFieldClass = ReferencedCollectionClass.get(99, listOfIntegers);
        SQL.insert(collectionAndAdditionalFieldClass, ReferencedCollectionClass.class, referencedCollectionFieldTable);
    
        assertRowCount(referencedCollectionFieldTable, 1L);
        assertTableContains(referencedCollectionFieldTable,
                Expected.column("additionalfield").value("99")
        );
        final @Nonnull String referencedColumnName = "_" + referencedCollectionFieldTable.getName().tableName + "_additionalfield";
        
        assertRowCount("TEST_HOST.LISTOFINTEGERS", 5L);
        assertTableContains("TEST_HOST.LISTOFINTEGERS",
                Expected.column("listofintegers").value("1").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("2").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("3").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("4").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("5").and(referencedColumnName).value("99")
        );
    }
    
}
