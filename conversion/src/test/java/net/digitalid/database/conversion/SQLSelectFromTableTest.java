package net.digitalid.database.conversion;

// TODO

//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import net.digitalid.utility.annotations.method.Impure;
//import net.digitalid.utility.collections.list.FreezableArrayList;
//import net.digitalid.utility.exceptions.ExternalException;
//import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
//
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClass;
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassConverter;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClass;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassBuilder;
//import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
//import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
//import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
//import net.digitalid.database.exceptions.DatabaseException;
//import net.digitalid.database.testing.SQLTestBase;
//import net.digitalid.database.unit.Unit;
//
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class SQLSelectFromTableTest extends SQLTestBase {
//    
//    private static TableImplementation simpleBooleanTable;
//    private static TableImplementation simpleCollectionsTable;
//    private static TableImplementation collectionsAndAdditionalFieldClassTable;
//    
//    private static final @Nonnull Unit unit = Unit.DEFAULT;
//    
//    @Impure
//    @BeforeClass
//    public static void createAndInsertTables() throws Exception {
//        SQLTestBase.setUpSQL();
//        simpleBooleanTable = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, unit);
//        simpleCollectionsTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, unit);
//        collectionsAndAdditionalFieldClassTable = SQL.create(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//    }
//    
//    @Impure
//    @AfterClass
//    public static void tearDown() throws DatabaseException {
//        dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
//        dropTable(SimpleCollectionsClassConverter.INSTANCE, unit);
//        dropTable(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//    }
//    
//    @Impure
//    private void insertSingleBoolean(boolean value) throws ExternalException {
//        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(value);
//        SQL.insert(convertibleObject, SingleBooleanColumnTableConverter.INSTANCE, unit);
//    
//        assertTableContains(simpleBooleanTable, unit, Expected.column("value").value(Boolean.toString(value).toUpperCase()));
//    }
//    
//    @Test
//    public void testSelectSingleBoolean() throws Exception {
//        insertSingleBoolean(true);
//        @Nullable SingleBooleanColumnTable singleBooleanColumnTable = SQL.select(SingleBooleanColumnTableConverter.INSTANCE, null, unit, null);
//
//        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", singleBooleanColumnTable);
//        Assert.assertEquals(true, singleBooleanColumnTable.value);
//        deleteFromTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
//
//        insertSingleBoolean(true);
//        insertSingleBoolean(false);
////        @Nullable SingleBooleanColumnTable singleBooleanColumnTable2 = SQL.select(SingleBooleanColumnTableConverter.INSTANCE, SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.EQUAL, SQLBooleanAlias.with("value"), SQLBooleanLiteral.get(false)), site);
//        @Nullable SingleBooleanColumnTable singleBooleanColumnTable2 = SQL.select(SingleBooleanColumnTableConverter.INSTANCE, SQLBooleanAlias.with("value").negated(), unit, null);
//
//        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", singleBooleanColumnTable2);
//        Assert.assertEquals(false, singleBooleanColumnTable2.value);
//        deleteFromTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
//    }
//
//    @Impure
//    private void insertSimpleCollection() throws ExternalException {
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
//    public void testEmbeddedList() throws Exception {
//        insertSimpleCollection();
//        @Nullable SimpleCollectionsClass simpleCollectionsClass = SQL.select(SimpleCollectionsClassConverter.INSTANCE, null, unit, null);
//
//        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", simpleCollectionsClass);
//        Assert.assertSame(5, simpleCollectionsClass.listOfIntegers.size());
//        deleteFromTable(SimpleCollectionsClassConverter.INSTANCE, unit);
//    }
//
//    @Impure
//    private void insertCollectionAndAdditionalField() throws ExternalException {
//        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
//        final @Nonnull CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = CollectionAndAdditionalFieldClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
//        SQL.insert(collectionAndAdditionalFieldClass, CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//
//        assertRowCount(collectionsAndAdditionalFieldClassTable, unit, 5L);
//        assertTableContains(collectionsAndAdditionalFieldClassTable, unit,
//                Expected.column("listofintegers").value("1").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("2").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("3").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("4").and("additionalfield").value("99"),
//                Expected.column("listofintegers").value("5").and("additionalfield").value("99"));
//    }
//
//    @Test
//    public void testEmbeddedListAndAdditionalField() throws Exception {
//        insertCollectionAndAdditionalField();
//        final @Nullable CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = SQL.select(CollectionAndAdditionalFieldClassConverter.INSTANCE, null, unit, null);
//
//        Assert.assertNotNull("Expected an instance of the CollectionAndAdditionalFieldClass type, but got null.", collectionAndAdditionalFieldClass);
//        Assert.assertSame(5, collectionAndAdditionalFieldClass.listOfIntegers.size());
//        Assert.assertSame(1, collectionAndAdditionalFieldClass.listOfIntegers.get(0));
//        Assert.assertSame(2, collectionAndAdditionalFieldClass.listOfIntegers.get(1));
//        Assert.assertSame(3, collectionAndAdditionalFieldClass.listOfIntegers.get(2));
//        Assert.assertSame(4, collectionAndAdditionalFieldClass.listOfIntegers.get(3));
//        Assert.assertSame(5, collectionAndAdditionalFieldClass.listOfIntegers.get(4));
//        Assert.assertSame(99, collectionAndAdditionalFieldClass.additionalField);
//        deleteFromTable(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//    }
//
//}
