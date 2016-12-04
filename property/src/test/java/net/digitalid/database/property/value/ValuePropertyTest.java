package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converters.StringConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Subject;
import net.digitalid.database.property.SubjectModule;
import net.digitalid.database.property.SubjectModuleBuilder;
import net.digitalid.database.property.annotations.GeneratePersistentProperty;
import net.digitalid.database.property.annotations.GenerateSubjectModule;
import net.digitalid.database.testing.SQLTestBase;

import org.junit.BeforeClass;
import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class ClassWithValueProperty extends RootClass implements Subject {
    
    /* -------------------------------------------------- Key -------------------------------------------------- */
    
    @Pure
    @PrimaryKey
    public abstract long getKey();
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    protected static final @Nonnull SubjectModule<ClassWithValueProperty> MODULE = SubjectModuleBuilder.<ClassWithValueProperty>withName("ClassWithValueProperty").withSubjectConverter(ClassWithValuePropertyConverter.INSTANCE).build();
    
    @Pure
    @GenerateSubjectModule
    protected void generateModule() {}
    
    /* -------------------------------------------------- Property -------------------------------------------------- */
    
    protected static final @Nonnull PersistentValuePropertyTable<ClassWithValueProperty, String, Void> table = PersistentValuePropertyTableBuilder.<ClassWithValueProperty, String, Void>withName("name").withParentModule(MODULE).withValueConverter(StringConverter.INSTANCE).withDefaultValue("default").withValueValidator(value -> value != null).build();
    
    protected final @Nonnull WritablePersistentValueProperty<ClassWithValueProperty, @Nonnull String> name = WritablePersistentValuePropertyBuilder.<ClassWithValueProperty, String>withSubject(this).withTable(table).build();
    
    @Pure
    @GeneratePersistentProperty
    public @Nonnull WritablePersistentValueProperty<ClassWithValueProperty, @Nonnull String> name() {
        return name;
    }
    
}

public class ValuePropertyTest extends SQLTestBase {
    
    static { runInMemory = true; }
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        System.out.println(ClassWithValueProperty.table.getEntryConverter().getName());
        SQL.create(ClassWithValueProperty.table.getEntryConverter(), Subject.DEFAULT_SITE);
    }
    
    @Test
    public void testProperty() throws DatabaseException {
        final @Nonnull ClassWithValueProperty object = ClassWithValuePropertyBuilder.withKey(123).build();
        object.name().set("test");
        object.name().reset();
        assertEquals("test", object.name().get());
    }

}
