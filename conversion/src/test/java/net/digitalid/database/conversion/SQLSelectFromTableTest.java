package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClass;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.CollectionAndAdditionalFieldClassConverter;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClass;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassBuilder;
import net.digitalid.database.conversion.testenvironment.iterable.SimpleCollectionsClassConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.dialect.ast.expression.bool.SQLBinaryBooleanExpression;
import net.digitalid.database.dialect.ast.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.ast.identifier.SQLBooleanAlias;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.storage.Site;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestHost;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class SQLSelectFromTableTest extends SQLTestBase {
    
    private static TableImplementation simpleBooleanTable;
    private static TableImplementation simpleCollectionsTable;
    private static TableImplementation collectionsAndAdditionalFieldClassTable;
    
    private static final Site site = new TestHost();
    
    @Impure
    @BeforeClass
    public static void createAndInsertTables() throws Exception {
        SQLTestBase.setUpSQL();
        simpleBooleanTable = SQL.create(SingleBooleanColumnTableConverter.INSTANCE, site);
        simpleCollectionsTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, site);
        collectionsAndAdditionalFieldClassTable = SQL.create(CollectionAndAdditionalFieldClassConverter.INSTANCE, site);
    }
    
    @Impure
    @AfterClass
    public static void tearDown() throws FailedNonCommittingOperationException {
        dropTable(SingleBooleanColumnTableConverter.INSTANCE, site);
        dropTable(SimpleCollectionsClassConverter.INSTANCE, site);
    }
    
    @Impure
    private void insertSingleBoolean(boolean value) throws ExternalException {
        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(value);
        SQL.insert(convertibleObject, SingleBooleanColumnTableConverter.INSTANCE, site);
    
        assertTableContains(simpleBooleanTable, site, Expected.column("value").value(Boolean.toString(value).toUpperCase()));
    }
    
    @Test
    public void testSelectSingleBoolean() throws Exception {
        insertSingleBoolean(true);
        @Nullable SingleBooleanColumnTable singleBooleanColumnTable = SQL.select(SingleBooleanColumnTableConverter.INSTANCE, null, site);

        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", singleBooleanColumnTable);
        Assert.assertEquals(true, singleBooleanColumnTable.value);
        deleteFromTable(SingleBooleanColumnTableConverter.INSTANCE, site);

        insertSingleBoolean(true);
        insertSingleBoolean(false);
        @Nullable SingleBooleanColumnTable singleBooleanColumnTable2 = SQL.select(SingleBooleanColumnTableConverter.INSTANCE, SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.EQUAL, SQLBooleanAlias.with("value"), SQLBooleanLiteral.get(false)), site);

        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", singleBooleanColumnTable2);
        Assert.assertEquals(false, singleBooleanColumnTable2.value);
        deleteFromTable(SingleBooleanColumnTableConverter.INSTANCE, site);
    }

    @Impure
    private void insertSimpleCollection() throws ExternalException {
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
    public void testEmbeddedList() throws Exception {
        insertSimpleCollection();
        @Nullable SimpleCollectionsClass simpleCollectionsClass = SQL.select(SimpleCollectionsClassConverter.INSTANCE, null, site);

        Assert.assertNotNull("Expected an instance of the SingleBooleanColumnTable type, but got null.", simpleCollectionsClass);
        Assert.assertSame(5, simpleCollectionsClass.listOfIntegers.size());
        deleteFromTable(SimpleCollectionsClassConverter.INSTANCE, site);
    }

    @Impure
    private void insertCollectionAndAdditionalField() throws ExternalException {
        final @Nonnull @NonNullableElements FreezableArrayList<Integer> listOfIntegers = FreezableArrayList.withElements(1, 2, 3, 4, 5);
        final @Nonnull CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = CollectionAndAdditionalFieldClassBuilder.withListOfIntegers(listOfIntegers).withAdditionalField(99).build();
        SQL.insert(collectionAndAdditionalFieldClass, CollectionAndAdditionalFieldClassConverter.INSTANCE, site);

        assertRowCount(collectionsAndAdditionalFieldClassTable, site, 5L);
        assertTableContains(collectionsAndAdditionalFieldClassTable, site,
                Expected.column("listofintegers").value("1").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("2").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("3").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("4").and("additionalfield").value("99"),
                Expected.column("listofintegers").value("5").and("additionalfield").value("99"));
    }

    @Test
    public void testEmbeddedListAndAdditionalField() throws Exception {
        insertCollectionAndAdditionalField();
        final @Nullable CollectionAndAdditionalFieldClass collectionAndAdditionalFieldClass = SQL.select(CollectionAndAdditionalFieldClassConverter.INSTANCE, null, site);

        Assert.assertNotNull("Expected an instance of the CollectionAndAdditionalFieldClass type, but got null.", collectionAndAdditionalFieldClass);
        Assert.assertSame(5, collectionAndAdditionalFieldClass.listOfIntegers.size());
        Assert.assertSame(1, collectionAndAdditionalFieldClass.listOfIntegers.get(0));
        Assert.assertSame(2, collectionAndAdditionalFieldClass.listOfIntegers.get(1));
        Assert.assertSame(3, collectionAndAdditionalFieldClass.listOfIntegers.get(2));
        Assert.assertSame(4, collectionAndAdditionalFieldClass.listOfIntegers.get(3));
        Assert.assertSame(5, collectionAndAdditionalFieldClass.listOfIntegers.get(4));
        Assert.assertSame(99, collectionAndAdditionalFieldClass.additionalField);
        deleteFromTable(CollectionAndAdditionalFieldClassConverter.INSTANCE, site);
    }

}
