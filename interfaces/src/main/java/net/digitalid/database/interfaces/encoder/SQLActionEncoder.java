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

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;

/**
 * The SQL action encoder helps to prepare statements that are considered
 * to manipulate the data set of the SQL database. It can handle the following statements:
 * - INSERT
 * - UPDATE
 * - DELETE
 * Although, by definition, the SELECT statement also falls under the DML category, we treat it
 * separately, because the return value of the execute method is different and, technically, SELECT
 * does not manipulate the data set of the database.
 * 
 * @see SQLQueryEncoder
 */
@Mutable
public interface SQLActionEncoder extends SQLEncoder {
    
    /**
     * Executes the data manipulation language (DML) statement of the type:
     * - INSERT
     * - UPDATE
     * - DELETE
     */
    @PureWithSideEffects
    public abstract void execute() throws DatabaseException;
    
}
