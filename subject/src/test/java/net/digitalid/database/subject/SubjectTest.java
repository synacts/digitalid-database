package net.digitalid.database.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.testing.RootTest;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.subject.site.SimpleSite;

import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
abstract class TestSubject extends RootClass implements Subject<SimpleSite> {}

public class SubjectTest extends RootTest {
    
    @Test
    public void testGeneratedModule() {
        final @Nonnull TestSubject subject = TestSubjectBuilder.withSite(SimpleSite.INSTANCE).build();
        assertEquals("TestSubject", subject.module().getName());
    }
    
}
