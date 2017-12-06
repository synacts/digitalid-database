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
package net.digitalid.database.mysql;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.number.SQLCurrentTime;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.statement.insert.SQLConflictClause;
import net.digitalid.database.dialect.statement.table.create.SQLType;

/**
 * This class implements the MySQL dialect.
 */
@Stateless
@GenerateSubclass
public abstract class MySQLDialect extends SQLDialect {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new MySQLDialectSubclass());
    }
    
    /* -------------------------------------------------- Unparsing -------------------------------------------------- */
    
    @Pure
    @TODO(task = "It might be necessary to use BINARY(17) instead of BINARY(16) for BINARY128 and BINARY(33) instead of BINARY(32) for BINARY256.", date = "2017-03-07", author = Author.KASPAR_ETTER)
    protected void unparse(@Nonnull SQLType type, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        type.unparse(this, unit, string);
        if (type.getType() == CustomType.STRING64 || type.getType() == CustomType.STRING128 || type.getType() == CustomType.STRING) {
            string.append(" COLLATE utf16_bin");
        }
    }
    
    @Pure
    protected void unparse(@Nonnull SQLIdentifier identifier, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("`").append(identifier.getString()).append("`");
    }
    
    @Pure
    @TODO(task = "Should we throw an exception in the other cases?", date = "2017-03-07", author = Author.KASPAR_ETTER)
    protected void unparse(@Nonnull SQLConflictClause conflictClause, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (conflictClause == SQLConflictClause.REPLACE) { string.append("REPLACE"); }
        else if (conflictClause == SQLConflictClause.IGNORE) { string.append("INSERT IGNORE"); }
        else { string.append("INSERT"); }
    }
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLType) { unparse((SQLType) node, unit, string); }
        else if (node instanceof SQLIdentifier) { unparse((SQLIdentifier) node, unit, string); }
        else if (node instanceof SQLConflictClause) { unparse((SQLConflictClause) node, unit, string); }
        else if (node instanceof SQLCurrentTime) { string.append("UNIX_TIMESTAMP(SYSDATE()) * 1000 + MICROSECOND(SYSDATE(3)) DIV 1000"); } // TODO: Is it important that it is the UNIX timestamp? Maybe we could just define another column type.
        else { super.unparse(node, unit, string); }
    }
    
    /* -------------------------------------------------- TODO -------------------------------------------------- */
    
    @Pure
    @Deprecated
    @TODO(task = "How shall we model this?", date = "2017-03-07", author = Author.KASPAR_ETTER)
    public @Nonnull String PRIMARY_KEY() {
        return "BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY";
    }
    
    /**
     * Returns the syntax for creating an index inside a table declaration.
     * 
     * @param columns the columns for which the index is to be created.
     */
    @Pure
    @Deprecated
    @TODO(task = "Do we still need this? If yes, create a corresponding table constraint.", date = "2017-03-07", author = Author.KASPAR_ETTER)
    public @Nonnull String INDEX(@Nonnull @NonNullableElements @NonEmpty String... columns) {
        final @Nonnull StringBuilder string = new StringBuilder(", INDEX(");
        for (final @Nonnull String column : columns) {
            if (string.length() != 8) { string.append(", "); }
            string.append(column);
        }
        return string.append(")").toString();
    }
    
}
