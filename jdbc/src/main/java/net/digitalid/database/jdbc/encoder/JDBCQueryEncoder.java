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
package net.digitalid.database.jdbc.encoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.logging.Log;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.jdbc.decoder.JDBCDecoderBuilder;

/**
 * The JDBC query encoder collects values for the prepared statement and executes it. Upon successful execution 
 * it returns an SQL decoder, which can be used to recover the data.
 */
@GenerateBuilder
@GenerateSubclass
public class JDBCQueryEncoder extends JDBCEncoderSubclass implements SQLQueryEncoder {
    
    protected JDBCQueryEncoder(@Nonnull PreparedStatement preparedStatement) {
        super(preparedStatement);
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
       
    @Override
    @PureWithSideEffects
    public @Nonnull SQLDecoder execute() throws DatabaseException {
        try {
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
            Log.verbose("Executed the prepared query statement.");
            return JDBCDecoderBuilder.withResultSet(resultSet).build();
        } catch (SQLException exception) {
            Log.debugging("Failed to execute the prepared query statement.", exception);
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
