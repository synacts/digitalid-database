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
package net.digitalid.database.client;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.file.Files;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.interfaces.Database;
import net.digitalid.database.jdbc.JDBCDatabaseBuilder;

import org.sqlite.JDBC;

/**
 * This class initializes the database for the client.
 */
@Utility
public abstract class ClientDatabaseInitializer {
    
    /* -------------------------------------------------- Configuration -------------------------------------------------- */
    
    /**
     * Stores the file name of the SQLite database (without the suffix).
     */
    public static final @Nonnull Configuration<String> fileName = Configuration.with("client");
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the database with the configured SQLite file.
     */
    @PureWithSideEffects
    @Initialize(target = Database.class, dependencies = {Files.class, ClientDatabaseInitializer.class})
    public static void initializeDatabase() throws SQLException {
        final @Nonnull String URL = "jdbc:sqlite:" + Files.relativeToConfigurationDirectory(fileName.get() + ".db");
        Database.instance.set(JDBCDatabaseBuilder.withDriver(new JDBC()).withURL(URL).build());
    }
    
}
