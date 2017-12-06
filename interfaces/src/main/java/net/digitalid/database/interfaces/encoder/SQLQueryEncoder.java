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
package net.digitalid.database.interfaces.encoder;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.SQLDecoder;

/**
 * The SQL query encoder helps to prepare statements that are considered to query the data set of
 * the SQL database. It executes SELECT statements.
 * 
 * We treat the SELECT statement different from the SQL Action encoder, 
 * because the return value of the execute method is different and, technically, SELECT
 * does not manipulate the data set of the database (even though SELECT is defined as a DML statement).
 * 
 * @see SQLActionEncoder
 */
@Mutable
public interface SQLQueryEncoder extends SQLEncoder {
    
    /**
     * Executes the SELECT statement.
     */
    @PureWithSideEffects
    public abstract @Nonnull SQLDecoder execute() throws DatabaseException;
    
}
