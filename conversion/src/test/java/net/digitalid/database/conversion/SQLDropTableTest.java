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
package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.DatabaseTest;

import org.junit.Test;

public class SQLDropTableTest extends DatabaseTest {
    
    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
    @Test
    public void shouldDropTableWithSingleBooleanColumn() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    
        SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableDoesNotExist(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    }
    
}
