package net.digitalid.database.subject.site;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.testing.RootTest;
import net.digitalid.utility.validation.annotations.type.Immutable;

import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
abstract class TestSite extends RootClass implements Site {}

public class SiteTest extends RootTest {
    
    @Test
    public void testGeneratedModule() {
        final @Nonnull TestSite site = TestSiteBuilder.withSchemaName("net.digitalid.test").build();
        assertEquals("TestSite", site.module().getName());
    }
    
}
