package net.digitalid.database.property.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.metadata.PrimaryKey;
import net.digitalid.database.conversion.SQL;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.ObjectModule;
import net.digitalid.database.property.ObjectModuleBuilder;
import net.digitalid.database.property.PropertyTable;
import net.digitalid.database.property.PropertyTableBuilder;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.testing.TestHost;

import org.junit.BeforeClass;
import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class CustomString {
    
    @Pure
    public abstract @Nonnull @MaxSize(50) String getContent();
    
}

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class ClassWithSimpleProperty extends RootClass {
    
    /* -------------------------------------------------- Key -------------------------------------------------- */
    
    @Pure
    @PrimaryKey
    public abstract long getKey();
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    @TODO(task = "Why is the parent module not optional?", date = "2016-08-30", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA)
    protected static final @Nonnull ObjectModule<ClassWithSimpleProperty> MODULE = ObjectModuleBuilder.<ClassWithSimpleProperty>withName("ClassWithSimpleProperty").withConverter(ClassWithSimplePropertyConverter.INSTANCE).build();
    
    @Pure
//    @GenerateModule
    protected void generateModule() {}
    
    /* -------------------------------------------------- Property -------------------------------------------------- */
    
    protected static final @Nonnull SimplePropertyEntryConverter<ClassWithSimpleProperty, CustomString> converter = SimplePropertyEntryConverterBuilder.<ClassWithSimpleProperty, CustomString>withName("ClassWithSimpleProperty_name").withObjectConverter(ClassWithSimplePropertyConverter.INSTANCE).withValueConverter(CustomStringConverter.INSTANCE).build();
    
    protected static final @Nonnull PropertyTable<ClassWithSimpleProperty, CustomString> table = PropertyTableBuilder.<ClassWithSimpleProperty, CustomString>withName("name").withParentModule(MODULE).withConverter(converter).withValueValidator(Predicate.ALWAYS_TRUE).build();
    
    protected final @Nonnull PersistentWritableSimpleProperty<@Nonnull CustomString> name = SimpleObjectPropertyBuilder.<ClassWithSimpleProperty, CustomString>withTable(table).withObject(this).withValueValidator(Predicate.ALWAYS_TRUE).build();
    
    // TODO: Where can we specify the default value?
    
    @Pure
//    @GenerateProperty
    public @Nonnull PersistentWritableSimpleProperty<@Nonnull CustomString> name() {
        return name;
    }
    
}

public class SimplePropertyTest extends SQLTestBase {
    
    @Impure
    @BeforeClass
    public static void createTables() throws Exception {
        SQL.create(ClassWithSimpleProperty.converter, new TestHost());
    }
    
    @Test
    public void testProperty() throws DatabaseException {
        final @Nonnull ClassWithSimpleProperty object = ClassWithSimplePropertyBuilder.withKey(123).build(); // TODO: Generate also .buildWithKey(123);
        object.name().set(CustomStringBuilder.withContent("test").build());
        object.name().reset();
        assertEquals("test", object.name().get().getContent());
    }

}
