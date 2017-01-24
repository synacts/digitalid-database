package net.digitalid.database.conversion;

// TODO

//import javax.annotation.Nonnull;
//
//import net.digitalid.utility.annotations.method.Impure;
//import net.digitalid.utility.annotations.method.Pure;
//import net.digitalid.utility.collections.list.FreezableArrayList;
//import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
//
//import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
//import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
//import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
//import net.digitalid.database.conversion.testenvironment.embedded.Convertible1Builder;
//import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
//import net.digitalid.database.conversion.testenvironment.embedded.Convertible2Builder;
//import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
//import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesBuilder;
//import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClass;
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClass;
//import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.CompositeCollectionsClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClass;
//import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.ReferencedCollectionClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClass;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
//import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTable;
//import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
//import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
//import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
//import net.digitalid.database.interfaces.Database;
//import net.digitalid.database.testing.SQLTestBase;
//import net.digitalid.database.unit.Unit;
//
//import org.h2.jdbc.JdbcBatchUpdateException;
//import org.hamcrest.BaseMatcher;
//import org.hamcrest.Description;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//public class SQLInsertTableTest extends SQLTestBase {
//    
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//    
//    private static TableImplementation simpleBooleanTable;
//    private static TableImplementation multiColumnBooleanTable;
//    private static TableImplementation constraintIntegerColumnTable;
//    private static TableImplementation propertyTable;
//    private static TableImplementation convertibleTable;
//    private static TableImplementation simpleCollectionsTable;
//    private static TableImplementation collectionAndAdditionalFieldTable;
//    private static TableImplementation compositeCollectionTable;
//    private static TableImplementation referencedCollectionFieldTable;
//    
//    private static final @Nonnull Unit unit = Unit.DEFAULT;
//    
//    @Impure
//    @BeforeClass
//    public static void createTables() throws Exception {
//        SQLTestBase.setUpSQL();
//        simpleBooleanTable = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, unit);
//        multiColumnBooleanTable = SQL.create(MultiBooleanColumnTableConverter.INSTANCE, unit);
//        constraintIntegerColumnTable = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        //        propertyTable = SQL.create(PropertyTable.class, site);
//        convertibleTable = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, unit);
//        simpleCollectionsTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, unit);
//        collectionAndAdditionalFieldTable = SQL.create(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//        compositeCollectionTable = SQL.create(CompositeCollectionsClassConverter.INSTANCE, unit);
//        referencedCollectionFieldTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, unit);
//    }
//    
//    @Impure
//    @AfterClass
//    public static void tearDown() throws FailedNonCommittingOperationException {
//        dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
//        dropTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
//        dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        dropTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
//        dropTable(SimpleCollectionsClassConverter.INSTANCE, unit);
//        dropTable(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//        dropTable(CompositeCollectionsClassConverter.INSTANCE, unit);
//        dropTable(ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_listofintegers", unit);
//        dropTable(ReferencedCollectionClassConverter.INSTANCE, unit);
//    }
//    
//    @Pure
//    @Before
//    public void deleteTableContent() throws FailedNonCommittingOperationException, EntryNotFoundException {
//        Database instance = Database.instance.get();
//        instance.execute("DELETE FROM " + unit.getName()+ "." + simpleBooleanTable.getName().toUpperCase());
//    }
//    
//    @Test
//    public void shouldInsertIntoSimpleBooleanTable() throws Exception {
//        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
//        SQL.insert(convertibleObject, SingleBooleanColumnTableConverter.INSTANCE, unit);
//
//        assertRowCount(simpleBooleanTable, unit, 1);
//        assertTableContains(simpleBooleanTable, unit, Expected.column("value").value("TRUE"));
//    }
//    
//    @Test
//    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
//        final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
//        SQL.insert(convertibleObject, MultiBooleanColumnTableConverter.INSTANCE, unit);
//        
//        assertRowCount(multiColumnBooleanTable, unit, 1);
//        assertTableContains(multiColumnBooleanTable, unit, Expected.column("firstvalue").value("TRUE"), Expected.column("secondValue").value("FALSE"));
//    }
//    
//    @Test
//    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
//        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
//        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        
//        assertRowCount(constraintIntegerColumnTable, unit, 1);
//        assertTableContains(constraintIntegerColumnTable, unit, Expected.column("value").value("14"));
//    }
//    
//    @Test
//    public void shouldNotInsertIntoConstraintIntegerColumnTable() throws Exception {
//        expectedException.expect(FailedUpdateExecutionException.class);
//        expectedException.expectMessage("A database operation failed.");
//        expectedException.expectCause(new BaseMatcher<Throwable>() {
//            
//            @Override
//            public boolean matches(Object o) {
//                if (o == null || !(o instanceof JdbcBatchUpdateException)) {
//                    return false;
//                }
//                final @Nonnull JdbcBatchUpdateException exception = (JdbcBatchUpdateException) o;
//                return exception.getMessage().startsWith("Check constraint violation:");
//            }
//    
//            @Override
//            public void describeTo(Description description) {
//                description.appendText(JdbcBatchUpdateException.class.getName() + ": Check constraint violation: ...");
//            }
//            
//        });
//        
//        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(2);
//        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//    }
//    
////    @Test
////    public void shouldInsertIntoTableWithProperty() throws Exception {
////        final @Nonnull PropertyTable convertible = PropertyTable.get();
////        convertible.myProperty.set(true);
////        SQL.insert(convertible, PropertyTable.class, propertyTable);
////    
////        assertRowCount(propertyTable, 1);
////        assertTableContains(propertyTable, Expected.column("myproperty").value("TRUE"));
////    }
////    
//    @Test
//    public void shouldInsertIntoTableWithEmbeddedConvertibles() throws Exception {
//        final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue1(2).build();
//        final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue2(3).build();
//        final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
//        SQL.insert(embeddedConvertibles, EmbeddedConvertiblesConverter.INSTANCE, unit);
//    
//        assertRowCount(convertibleTable, unit, 1);
//        assertTableContains(convertibleTable, unit, Expected.column("value1").value("2"), Expected.column("value2").value("3"));
//    }
//    
//    @Test
//    public void shouldInsertIntoTableWithSimpleCollectionsClass() throws Exception {
//        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
//        final @Nonnull SimpleCollectionsClass simpleCollectionsClass = SimpleCollectionsClassBuilder.withListOfIntegers(listOfIntegers).build();
//        SQL.insert(simpleCollectionsClass, SimpleCollectionsClassConverter.INSTANCE, unit);
//
//        assertRowCount(simpleCollectionsTable, unit, 5L);
//        assertTableContains(simpleCollectionsTable, unit,
//                Expected.column("listofintegers").value("1"),
//                Expected.column("listofintegers").value("2"),
//                Expected.column("listofintegers").value("3"),
//                Expected.column("listofintegers").value("4"),
//                Expected.column("listofintegers").value("5"));
//    }
//    
//    @Test
//    public void shouldInsertIntoTableWithCollectionAndAdditionalFieldClass() throws Exception {
//        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
//        final @Nonnull CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = CollectionAndAdditionalFieldClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
//        SQL.insert(collectionAndAdditionalFieldClass, CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//    
//        assertRowCount(collectionAndAdditionalFieldTable, unit, 5L);
//        assertTableContains(collectionAndAdditionalFieldTable, unit,
//                Expected.column("listofintegers").value("1").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("2").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("3").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("4").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("5").and("additionalfield").value("99")
//                );
//    }
//    
//    @Test
//    public void shouldInsertIntoTableWithCompositeCollection() throws Exception {
//        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers1 = FreezableArrayList.withElements(1, 2, 3, 4, 5);
//        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers2 = FreezableArrayList.withElements(100, 200, 300, 400);
//        
//        final @Nonnull @NonNullableElements FreezableArrayList<FreezableArrayList<Integer>> listOfListOfIntegers = FreezableArrayList.withElements(listOfIntegers1, listOfIntegers2);
//        
//        final @Nonnull CompositeCollectionsClass collectionAndAdditionalFieldClass = CompositeCollectionsClassBuilder.withListOfListOfIntegers(listOfListOfIntegers).build();
//        SQL.insert(collectionAndAdditionalFieldClass, CompositeCollectionsClassConverter.INSTANCE, unit);
//        
//        assertRowCount(compositeCollectionTable, unit, 9L);
//        assertTableContains(compositeCollectionTable, unit,
//                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("0").and("listoflistofintegers").value("1"),
//                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("1").and("listoflistofintegers").value("2"),
//                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("2").and("listoflistofintegers").value("3"),
//                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("3").and("listoflistofintegers").value("4"),
//                Expected.column("_listoflistofintegers_index_0").value("0").and("_listoflistofintegers_index_1").value("4").and("listoflistofintegers").value("5"),
//                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("0").and("listoflistofintegers").value("100"),
//                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("1").and("listoflistofintegers").value("200"),
//                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("2").and("listoflistofintegers").value("300"),
//                Expected.column("_listoflistofintegers_index_0").value("1").and("_listoflistofintegers_index_1").value("3").and("listoflistofintegers").value("400")
//        );
//    }
//    
//    @Test
//    public void shouldInsertIntoTableWithReferencedCollection() throws Exception {
//        final @Nonnull FreezableArrayList<@Nonnull Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
//        final @Nonnull ReferencedCollectionClass collectionAndAdditionalFieldClass = ReferencedCollectionClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
//        SQL.insert(collectionAndAdditionalFieldClass, ReferencedCollectionClassConverter.INSTANCE, unit);
//
//        assertRowCount(referencedCollectionFieldTable, unit, 1L);
//        assertTableContains(referencedCollectionFieldTable, unit,
//                Expected.column("additionalfield").value("99")
//        );
//        final @Nonnull String referencedColumnName = "additionalfield";
//
//        assertRowCount(Unit.DEFAULT.getName()+ "." + ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_LISTOFINTEGERS", 5L);
//        assertTableContains(Unit.DEFAULT.getName()+ "." + ReferencedCollectionClassConverter.INSTANCE.getTypeName() + "_LISTOFINTEGERS",
//                Expected.column("listofintegers").value("1").and(referencedColumnName).value("99"),
//                Expected.column("listofintegers").value("2").and(referencedColumnName).value("99"),
//                Expected.column("listofintegers").value("3").and(referencedColumnName).value("99"),
//                Expected.column("listofintegers").value("4").and(referencedColumnName).value("99"),
//                Expected.column("listofintegers").value("5").and(referencedColumnName).value("99")
//        );
//    }
//    
//}
