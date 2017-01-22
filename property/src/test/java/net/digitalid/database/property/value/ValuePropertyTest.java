package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.annotations.GeneratePersistentProperty;
import net.digitalid.database.unit.DefaultUnit;
import net.digitalid.database.testing.SQLTestBase;

import org.junit.BeforeClass;
import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class ClassWithValueProperty extends RootClass implements Subject<DefaultUnit> {
    
    /* -------------------------------------------------- Key -------------------------------------------------- */
    
    @Pure
    @PrimaryKey
    public abstract long getKey();
    
    @Pure
    @Default("\"default\"")
    @GeneratePersistentProperty
    public abstract @Nonnull WritablePersistentValueProperty<ClassWithValueProperty, @Nonnull String> name();
    
}

public class ValuePropertyTest extends SQLTestBase {
    
    static { runInMemory = true; }
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        System.out.println(ClassWithValuePropertySubclass.NAME_TABLE.getEntryConverter().getTypeName());
        SQL.create(ClassWithValuePropertySubclass.NAME_TABLE.getEntryConverter(), Unit.DEFAULT);
    }
    
    @Test
    public void testProperty() throws DatabaseException {
        final @Nonnull ClassWithValueProperty object = ClassWithValuePropertyBuilder.withSite(Unit.DEFAULT).withKey(123).build();
        object.name().set("test");
        object.name().reset();
        assertEquals("test", object.name().get());
    }

}
