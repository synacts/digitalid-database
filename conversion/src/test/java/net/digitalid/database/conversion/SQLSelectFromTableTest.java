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
import javax.annotation.Nullable;

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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests whether SQL select statements works.
 */
public class SQLSelectFromTableTest extends DatabaseTest {

    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
    /**
     * Tests whether select works on rows that match the where-object completely.
     */
    @Test
    public void shouldSelectFromSimpleBooleanTable() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        try {
            final @Nonnull SingleBooleanColumnTable convertibleObject = SingleBooleanColumnTable.get(true);
            SQL.insertOrAbort(SingleBooleanColumnTableConverter.INSTANCE, convertibleObject, unit);
    
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
    
            final @Nonnull WhereCondition<SingleBooleanColumnTable> whereCondition = WhereConditionBuilder.withConverter(SingleBooleanColumnTableConverter.INSTANCE).withObject(convertibleObject).build();
            final @Nullable SingleBooleanColumnTable singleBooleanColumnTable = SQL.selectFirst(SingleBooleanColumnTableConverter.INSTANCE, null, unit, whereCondition);
    
            Assert.assertNotNull(singleBooleanColumnTable);
            Assert.assertTrue("Expected the value 'true' in the single boolean column table.", singleBooleanColumnTable.value);
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        } finally {
            SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        }
    }
    
    /**
     * Tests whether select works on rows with cells that match the where-object.
     */
    @Test
    public void shouldSelectFromTableWithEmbeddedConvertibles() throws Exception {
        SQL.createTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        try {
            final @Nonnull Convertible1 convertible1 = Convertible1Builder.withValue(2).build();
            final @Nonnull Convertible2 convertible2 = Convertible2Builder.withValue(3).build();
            final @Nonnull EmbeddedConvertibles embeddedConvertibles = EmbeddedConvertiblesBuilder.withConvertible1(convertible1).withConvertible2(convertible2).build();
            SQL.insertOrAbort(EmbeddedConvertiblesConverter.INSTANCE, embeddedConvertibles, unit);

            assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("convertible1_value").value("2"), Expected.column("convertible2_value").value("3"));

            final @Nonnull WhereCondition<Convertible1> whereCondition = WhereConditionBuilder.withConverter(Convertible1Converter.INSTANCE).withObject(convertible1).withPrefix("convertible1").build();
            final @Nullable EmbeddedConvertibles embeddedConvertiblesTable = SQL.selectFirst(EmbeddedConvertiblesConverter.INSTANCE, null, unit, whereCondition);
    
            Assert.assertNotNull(embeddedConvertiblesTable);
            Assert.assertEquals(embeddedConvertibles, embeddedConvertiblesTable);
            Assert.assertSame(2, embeddedConvertiblesTable.getConvertible1().getValue());
            Assert.assertSame(3, embeddedConvertiblesTable.getConvertible2().getValue());
            assertRowCount(EmbeddedConvertiblesConverter.INSTANCE.getTypeName(), unit.getName(), 1);
        } finally {
            SQL.dropTable(EmbeddedConvertiblesConverter.INSTANCE, unit);
        }
    }
    
    // TODO: add a test with a type that contains an Integer or String field and check whether the prefix is properly constructed.
}
