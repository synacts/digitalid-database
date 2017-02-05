package net.digitalid.database.conversion;

// TODO

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.FreezableArrayList;

import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTable;
import net.digitalid.database.conversion.testenvironment.columnconstraints.ConstraintIntegerColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1Builder;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2Builder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesBuilder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.MultiBooleanColumnTableConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.unit.Unit;

import org.h2.jdbc.JdbcBatchUpdateException;
import org.h2.jdbc.JdbcSQLException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
public class SQLInsertTableTest extends SQLTestBase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final @Nonnull Unit unit = Unit.DEFAULT;

//    @Impure
//    @BeforeClass
//    public static void createTables() throws Exception {
//        multiColumnBooleanTable = SQL.create(MultiBooleanColumnTableConverter.INSTANCE, unit);
//        constraintIntegerColumnTable = SQL.create(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
//        //        propertyTable = SQL.create(PropertyTable.class, site);
//        convertibleTable = SQL.create(EmbeddedConvertiblesConverter.INSTANCE, unit);
//        simpleCollectionsTable = SQL.create(SimpleCollectionsClassConverter.INSTANCE, unit);
//        collectionAndAdditionalFieldTable = SQL.create(CollectionAndAdditionalFieldClassConverter.INSTANCE, unit);
//        compositeCollectionTable = SQL.create(CompositeCollectionsClassConverter.INSTANCE, unit);
//        referencedCollectionFieldTable = SQL.create(ReferencedCollectionClassConverter.INSTANCE, unit);
//    }

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
    
    @Test
    public void shouldInsertIntoSimpleBooleanTable() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
        SQL.insert(convertibleObject, SingleBooleanColumnTableConverter.INSTANCE, unit);
        
        assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
    }
    
    @Test
    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
        SQL.createTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
        final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
        SQL.insert(convertibleObject, MultiBooleanColumnTableConverter.INSTANCE, unit);

        assertRowCount(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        assertTableContains(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("firstvalue").value("TRUE"), Expected.column("secondValue").value("FALSE"));
    }
    
    @Test
    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
        SQL.createTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, unit);
    
        assertRowCount(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        assertTableContains(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("14"));
    }

    @Test
    public void shouldNotInsertIntoConstraintIntegerColumnTable() throws Exception {
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("The conversion failed due to an interrupted connection or violated constraints.");
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
            public void describeTo(@Nonnull Description description) {
                description.appendText(JdbcBatchUpdateException.class.getName() + ": Check constraint violation: ...");
            }

        });

        final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(2);
        SQL.insert(convertibleObject, ConstraintIntegerColumnTableConverter.INSTANCE, unit);
    }
    
    // TODO: either implement here or move to property module.
//    @Test
//    public void shouldInsertIntoTableWithProperty() throws Exception {
//        final @Nonnull PropertyTable convertible = PropertyTable.get();
//        convertible.myProperty.set(true);
//        SQL.insert(convertible, PropertyTable.class, propertyTable);
//    
//        assertRowCount(propertyTable, 1);
//        assertTableContains(propertyTable, Expected.column("myproperty").value("TRUE"));
//    }

    @Test
    public void shouldInsertIntoTableWithEmbeddedConvertibles() throws Exception {
        SQL.createTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue(2).build();
        final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue(3).build();
        final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
        SQL.insert(embeddedConvertibles, EmbeddedConvertiblesConverter.INSTANCE, unit);

        assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        assertTableContains(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("convertible1_value").value("2"), Expected.column("convertible2_value").value("3"));
    }
    
}
