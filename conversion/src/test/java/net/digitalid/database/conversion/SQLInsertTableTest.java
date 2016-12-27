package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1Builder;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2Builder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesBuilder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClass;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClass;
import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClass;
import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClass;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.DatabaseInstance;
import net.digitalid.database.subject.site.SimpleSite;
import net.digitalid.database.testing.SQLTestBase;

import org.h2.jdbc.JdbcBatchUpdateException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SQLInsertTableTest extends SQLTestBase {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static TableImplementation simpleBooleanTable;
    private static TableImplementation multiColumnBooleanTable;
    private static TableImplementation constraintIntegerColumnTable;
    private static TableImplementation propertyTable;
    private static TableImplementation convertibleTable;
    private static TableImplementation simpleCollectionsTable;
    private static TableImplementation collectionAndAdditionalFieldTable;
    private static TableImplementation compositeCollectionTable;
    private static TableImplementation referencedCollectionFieldTable;
    
    private static final @Nonnull SimpleSite site = SimpleSite.INSTANCE;
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        SQLTestBase.setUpSQL();
        simpleBooleanTable = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, site);
        multiColumnBooleanTable = SQL.create(MultiBooleanColumnTableConverter.INSTANCE, site);
        constraintIntegerColumnTable = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, site);
        //        propertyTable = SQL.create(PropertyTable.class, site);
        convertibleTable = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, site);
        simpleCollectionsTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, site);
        collectionAndAdditionalFieldTable = SQL.create(CollectionAndAdditionalFieldClassConverter.INSTANCE, site);
        compositeCollectionTable = SQL.create(CompositeCollectionsClassConverter.INSTANCE, site);
        referencedCollectionFieldTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, site);
    }
    
    @Impure
    @AfterClass
    public static void tearDown() throws FailedNonCommittingOperationException {
        dropTable(SingleBooleanColumnTableConverter.INSTANCE, site);
        dropTable(MultiBooleanColumnTableConverter.INSTANCE, site);
        dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, site);
        dropTable(EmbeddedConvertiblesConverter.INSTANCE, site);
        dropTable(SimpleCollectionsClassConverter.INSTANCE, site);
        dropTable(CollectionAndAdditionalFieldClassConverter.INSTANCE, site);
        dropTable(CompositeCollectionsClassConverter.INSTANCE, site);
        dropTable(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", site);
        dropTable(ReferencedCollectionClassConverter.INSTANCE, site);
    }
    
    @Pure
    @Before
    public void deleteTableContent() throws FailedNonCommittingOperationException, EntryNotFoundException {
        DatabaseInstance instance = Database.getInstance();
        instance.execute("DELETE FROM " + site.getSchemaName() + "." + simpleBooleanTable.getName().toUpperCase());
    }
    
    @Test
    public void shouldInsertIntoSimpleBooleanTable() throws Exception {
        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
        SQL.insert(convertibleObject, SingleBooleanColumnTableConverter.INSTANCE, site);

        assertRowCount(simpleBooleanTable, site, 1);
        assertTableContains(simpleBooleanTable, site, Expected.column("value").value("TRUE"));
    }
    
    @Test
    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
        final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
        SQL.insert(convertibleObject, MultiBooleanColumnTableConverter.INSTANCE, site);
        
        assertRowCount(multiColumnBooleanTable, site, 1);
        assertTableContains(multiColumnBooleanTable, site, Expected.column("firstvalue").value("TRUE"), Expected.column("secondValue").value("FALSE"));
    }
    
    @Test
    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, site);
        
        assertRowCount(constraintIntegerColumnTable, site, 1);
        assertTableContains(constraintIntegerColumnTable, site, Expected.column("value").value("14"));
    }
    
    @Test
    public void shouldNotInsertIntoConstraintIntegerColumnTable() throws Exception {
        expectedException.expect(FailedUpdateExecutionException.class);
        expectedException.expectMessage("A database operation failed.");
        expectedException.expectCause(new BaseMatcher<Throwable>() {
            
            @Override
            public boolean matches(Object o) {
                if (o == null || !(o instanceof JdbcBatchUpdateException)) {
                    return false;
                }
                final @Nonnull JdbcBatchUpdateException exception = (JdbcBatchUpdateException) o;
                return exception.getMessage().startsWith("Check constraint violation:");
            }
    
            @Override
            public void describeTo(Description description) {
                description.appendText(JdbcBatchUpdateException.class.getName() + ": Check constraint violation: ...");
            }
            
        });
        
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(2);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, site);
    }
    
//    @Test
//    public void shouldInsertIntoTableWithProperty() throws Exception {
//        final @Nonnull PropertyTable convertible = PropertyTable.get();
//        convertible.myProperty.set(true);
//        SQL.insert(convertible, PropertyTable.class, propertyTable);
//    
//        assertRowCount(propertyTable, 1);
//        assertTableContains(propertyTable, Expected.column("myproperty").value("TRUE"));
//    }
//    
    @Test
    public void shouldInsertIntoTableWithEmbeddedConvertibles() throws Exception {
        final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue1(2).build();
        final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue2(3).build();
        final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
        SQL.insert(embeddedConvertibles, EmbeddedConvertiblesConverter.INSTANCE, site);
    
        assertRowCount(convertibleTable, site, 1);
        assertTableContains(convertibleTable, site, Expected.column("value1").value("2"), Expected.column("value2").value("3"));
    }
    
    @Test
    public void shouldInsertIntoTableWithSimpleCollectionsClass() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
        final @Nonnull SimpleCollectionsClass simpleCollectionsClass = SimpleCollectionsClassBuilder.withListOfIntegers(listOfIntegers).build();
        SQL.insert(simpleCollectionsClass, SimpleCollectionsClassConverter.INSTANCE, site);

        assertRowCount(simpleCollectionsTable, site, 5L);
        assertTableContains(simpleCollectionsTable, site,
                Expected.column("listofintegers").value("1"),
                Expected.column("listofintegers").value("2"),
                Expected.column("listofintegers").value("3"),
                Expected.column("listofintegers").value("4"),
                Expected.column("listofintegers").value("5"));
    }
    
    @Test
    public void shouldInsertIntoTableWithCollectionAndAdditionalFieldClass() throws Exception {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
        final @Nonnull CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = CollectionAndAdditionalFieldClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
        SQL.insert(collectionAndAdditionalFieldClass, CollectionAndAdditionalFieldClassConverter.INSTANCE, site);
    
        assertRowCount(collectionAndAdditionalFieldTable, site, 5L);
        assertTableContains(collectionAndAdditionalFieldTable, site,
                Expected.column("listofintegers").value("1").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("2").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("3").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("4").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("5").and("additionalfield").value("99")
                );
    }
    
    @Test
    public void shouldInsertIntoTableWithCompositeCollection() throws Exception {
        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers1 = FreezableArrayList.withElements(1, 2, 3, 4, 5);
        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers2 = FreezableArrayList.withElements(100, 200, 300, 400);
        
        final @Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers = FreezableArrayList.withElements(listOfIntegers1, listOfIntegers2);
        
        final @Nonnull CompositeCollectionsClass collectionAndAdditionalFieldClass = CompositeCollectionsClassBuilder.withListOfListOfIntegers(listOfListOfIntegers).build();
        SQL.insert(collectionAndAdditionalFieldClass, CompositeCollectionsClassConverter.INSTANCE, site);
        
        assertRowCount(compositeCollectionTable, site, 9L);
        assertTableContains(compositeCollectionTable, site,
                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("0").and("listoflistofintegers").value("1"),
                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("1").and("listoflistofintegers").value("2"),
                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("2").and("listoflistofintegers").value("3"),
                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("3").and("listoflistofintegers").value("4"),
                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("4").and("listoflistofintegers").value("5"),
                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("0").and("listoflistofintegers").value("100"),
                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("1").and("listoflistofintegers").value("200"),
                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("2").and("listoflistofintegers").value("300"),
                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("3").and("listoflistofintegers").value("400")
        );
    }
    
    @Test
    public void shouldInsertIntoTableWithReferencedCollection() throws Exception {
        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
        final @Nonnull ReferencedCollectionClass collectionAndAdditionalFieldClass = ReferencedCollectionClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
        SQL.insert(collectionAndAdditionalFieldClass, ReferencedCollectionClassConverter.INSTANCE, site);

        assertRowCount(referencedCollectionFieldTable, site, 1L);
        assertTableContains(referencedCollectionFieldTable, site,
                Expected.column("additionalfield").value("99")
        );
        final @Nonnull String referencedColumnName = "additionalfield";

        assertRowCount(SimpleSite.INSTANCE.getSchemaName() + "." + ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_LISTOFINTEGERS", 5L);
        assertTableContains(SimpleSite.INSTANCE.getSchemaName() + "." + ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_LISTOFINTEGERS",
                Expected.column("listofintegers").value("1").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("2").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("3").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("4").and(referencedColumnName).value("99"),
                Expected.column("listofintegers").value("5").and(referencedColumnName).value("99")
        );
    }
    
}
