package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
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
import net.digitalid.database.property.set.WritablePersistentSimpleSetProperty;
import net.digitalid.database.property.value.WritablePersistentValueProperty;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.annotations.GeneratePersistentProperty;
import net.digitalid.database.testing.SQLTestBase;
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
    public abstract @Nonnull WritablePersistentSimpleSetProperty<Student, Student> friends();

//    @Pure
//    @GeneratePersistentProperty
//    public abstract @Nonnull WritablePersistentSimpleMapProperty<Student, @MaxSize(64) String, Integer> grades();
    
}

public class ValuePropertyTest extends SQLTestBase {
    
    private static final @Nonnull Student object = StudentBuilder.withKey(123).build();
    
    private static final @Nonnull Student friend = StudentBuilder.withKey(124).build();
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        SQL.createTable(StudentConverter.INSTANCE, Unit.DEFAULT);
        SQL.createTable(StudentSubclass.NAME_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.createTable(StudentSubclass.AGE_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.createTable(StudentSubclass.FRIENDS_TABLE.getEntryConverter(), Unit.DEFAULT);
        SQL.insert(StudentConverter.INSTANCE, object, Unit.DEFAULT);
        SQL.insert(StudentConverter.INSTANCE, friend, Unit.DEFAULT);
    }
    
    @Test
    public void testStringProperty() throws DatabaseException, RecoveryException {
        object.name().set("test");
        object.name().reset();
        assertEquals("test", object.name().get());
    }
    
    @Test
    public void testIntegerProperty() throws DatabaseException, RecoveryException {
        object.age().set(29);
        object.age().reset();
        assertSame(29, object.age().get());
    }
    
    @Test
    public void testFriendsProperty() throws DatabaseException, RecoveryException {
        object.friends().add(object);
        object.friends().add(friend);
        object.age().reset();
        final @Nonnull @NonFrozen ReadOnlySet<@Nonnull @Valid Student> friends = object.friends().get();
        assertSame(2, friends.size());
        assertTrue(friends.contains(object));
        assertTrue(friends.contains(friend));
    }
    
}
