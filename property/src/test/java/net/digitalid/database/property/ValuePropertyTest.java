package net.digitalid.database.property;

import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.map.WritablePersistentSimpleMapProperty;
import net.digitalid.database.property.set.WritablePersistentSimpleSetProperty;
import net.digitalid.database.property.value.WritablePersistentValueProperty;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.annotations.GeneratePersistentProperty;
import net.digitalid.database.testing.DatabaseTest;
import net.digitalid.database.unit.Unit;

import org.junit.BeforeClass;
import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class Student extends RootClass implements Subject<Unit> {
    
    /* -------------------------------------------------- Key -------------------------------------------------- */
    
    @Pure
    @PrimaryKey
    public abstract long getKey();
    
    @Pure
    @Default("\"default\"")
    @GeneratePersistentProperty
    public abstract @Nonnull WritablePersistentValueProperty<Student, @Nonnull String> name();
    
    @Pure
    @Default("0")
    @GeneratePersistentProperty
    public abstract @Nonnull WritablePersistentValueProperty<Student, @Nonnull Integer> age();
    
    @Pure
    @GeneratePersistentProperty
    @TODO(task = "Introduce a way to pass a 'provided object extractor' to the friends table that provides the unit of the subject to the value recovery .", date = "2017-02-17", author = Author.KASPAR_ETTER)
    public abstract @Nonnull WritablePersistentSimpleSetProperty<Student, Student> friends();

    @Pure
    @GeneratePersistentProperty
    public abstract @Nonnull WritablePersistentSimpleMapProperty<Student, Integer, Integer> grades();
    
}

public class ValuePropertyTest extends DatabaseTest {
    
    private static final @Nonnull Student object = StudentBuilder.withKey(123).build();
    
    private static final @Nonnull Student friend = StudentBuilder.withKey(124).build();
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        SQL.createTable(StudentConverter.INSTANCE, Unit.DEFAULT);
        SQL.createTable(StudentSubclass.NAME_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.createTable(StudentSubclass.AGE_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.createTable(StudentSubclass.FRIENDS_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.createTable(StudentSubclass.GRADES_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.insert(StudentConverter.INSTANCE, object, Unit.DEFAULT);
        SQL.insert(StudentConverter.INSTANCE, friend, Unit.DEFAULT);
    }
    
    @Test
    public void testStringProperty() throws DatabaseException, RecoveryException {
        object.name().set("test");
        object.name().reset();
        assertThat(object.name().get()).isEqualTo("test");
    }
    
    @Test
    public void testIntegerProperty() throws DatabaseException, RecoveryException {
        object.age().set(29);
        object.age().reset();
        assertThat(object.age().get()).isEqualTo(29);
    }
    
    @Test
    public void testFriendsProperty() throws DatabaseException, RecoveryException {
        object.friends().add(object);
        object.friends().add(friend);
        object.friends().reset();
        final @Nonnull @NonFrozen ReadOnlySet<@Nonnull @Valid Student> friends = object.friends().get();
        // TODO: Make the following work! assertThat(friends).as("friends").containsExactly/*InAnyOrder*/(object, friend); // TODO: Do we really guarantee the order in this case?
        assertThat(friends).as("friends").extracting("key").containsExactly(123l, 124l); // TODO: Remove this line afterwards.
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testGradesProperty() throws DatabaseException, RecoveryException {
        object.grades().add(1, 5);
        object.grades().add(2, 2);
        object.grades().reset();
        final @Nonnull Map<@Nonnull @Valid("key") Integer, @Nonnull @Valid Integer> grades = (Map<@Nonnull @Valid("key") Integer, @Nonnull @Valid Integer>) object.grades().get();
        assertThat(grades).as("grades").hasSize(2).containsKey(1).containsEntry(1, 5).containsEntry(2, 2);
    }
    
}
