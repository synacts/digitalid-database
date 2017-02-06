package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

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

public class SQLInsertTableTest extends SQLTestBase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final @Nonnull Unit unit = Unit.DEFAULT;

    @Test
    public void shouldInsertIntoSimpleBooleanTable() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        try {
            final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
            SQL.insert(SingleBooleanColumnTableConverter.INSTANCE, convertibleObject, unit);
    
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
        } finally {
            SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        }
    }
    
    @Test
    public void shouldInsertIntoMultiColumnBooleanTable() throws Exception {
        SQL.createTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
        try {
            final @Nonnull MultiBooleanColumnTable convertibleObject = MultiBooleanColumnTable.get(true, false);
            SQL.insert(MultiBooleanColumnTableConverter.INSTANCE, convertibleObject, unit);

            assertRowCount(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(MultiBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("firstvalue").value("TRUE"), Expected.column("secondValue").value("FALSE"));
        } finally {
            SQL.dropTable(MultiBooleanColumnTableConverter.INSTANCE, unit);
        }
    }
    
    @Test
    public void shouldInsertIntoConstraintIntegerColumnTable() throws Exception {
        SQL.createTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        try {
            final @Nonnull ConstraintIntegerColumnTable convertibleObject = ConstraintIntegerColumnTable.get(14);
            SQL.insert(ConstraintIntegerColumnTableConverter.INSTANCE, convertibleObject, unit);
    
            assertRowCount(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(ConstraintIntegerColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("14"));
        } finally {
            SQL.dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        }
    }

    @Test
    public void shouldNotInsertIntoConstraintIntegerColumnTable() throws Exception {
        SQL.createTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        try {
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
            SQL.insert(ConstraintIntegerColumnTableConverter.INSTANCE, convertibleObject, unit);
        } finally {
            SQL.dropTable(ConstraintIntegerColumnTableConverter.INSTANCE, unit);
        }
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
        try {
            final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue(2).build();
            final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue(3).build();
            final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
            SQL.insert(EmbeddedConvertiblesConverter.INSTANCE, embeddedConvertibles, unit);

            assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("convertible1_value").value("2"), Expected.column("convertible2_value").value("3"));
        } finally {
            SQL.dropTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        }
    }
    
}
