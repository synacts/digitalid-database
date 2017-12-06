/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.property.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.testing.UtilityTest;
import net.digitalid.utility.validation.annotations.type.Immutable;

import org.junit.Test;

@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateTableConverter
abstract class TestSubject extends Subject<Unit> {}

public class SubjectTest extends UtilityTest {
    
    @Test
    public void testGeneratedModule() {
        final @Nonnull TestSubject subject = TestSubjectBuilder.build();
        assertThat(subject.module().getName()).as("the name of the generated module").isEqualTo("TestSubject");
    }
    
}
