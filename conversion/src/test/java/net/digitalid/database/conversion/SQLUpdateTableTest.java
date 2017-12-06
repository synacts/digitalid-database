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

import net.digitalid.database.conversion.testenvironment.embedded.Convertible1;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1Builder;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible1Converter;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2;
import net.digitalid.database.conversion.testenvironment.embedded.Convertible2Builder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertibles;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesBuilder;
import net.digitalid.database.conversion.testenvironment.embedded.EmbeddedConvertiblesConverter;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.DatabaseTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SQLUpdateTableTest extends DatabaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final @Nonnull Unit unit = Unit.DEFAULT;

    @Test
    public void shouldUpdateSimpleBooleanTable() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        try {
            final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
            SQL.insertOrAbort(SingleBooleanColumnTableConverter.INSTANCE, convertibleObject, unit);
            
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
        
            final @Nonnull SingleBooleanColumnTable updatedObject = SingleBooleanColumnTable.get(false);
            SQL.update(SingleBooleanColumnTableConverter.INSTANCE, updatedObject, unit, WhereConditionBuilder.withConverter(SingleBooleanColumnTableConverter.INSTANCE).withObject(convertibleObject).build());
    
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("FALSE"));
    
        } finally {
            SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        }
    }
    
    @Test
    public void shouldUpdateTableWithEmbeddedConvertibles() throws Exception {
        SQL.createTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        try {
            final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue(2).build();
            final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue(3).build();
            final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
            SQL.insertOrAbort(EmbeddedConvertiblesConverter.INSTANCE, embeddedConvertibles, unit);
    
            assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("convertible1_value").value("2"), Expected.column("convertible2_value").value("3"));
    
            final @Nonnull EmbeddedConvertibles updatedEmbeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(Convertible1Builder.withValue(4).build()).withConvertible2(Convertible2Builder.withValue(5).build()).build();
            
            SQL.update(EmbeddedConvertiblesConverter.INSTANCE, updatedEmbeddedConvertibles, unit, WhereConditionBuilder.withConverter(Convertible1Converter.INSTANCE).withObject(convertible1).withPrefix("convertible1").build());
    
            assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("convertible1_value").value("4"), Expected.column("convertible2_value").value("5"));
    
        } finally {
            SQL.dropTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        }
    }
    
}
