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
package net.digitalid.database.access;

import net.digitalid.utility.validation.annotations.type.Stateless;

/**
 * The mode determines whether the database is accessed by a single process.
 */
@Stateless
public enum Mode {
    
    /**
     * In case of single-access, only one process accesses the
     * database, which allows to keep the objects in memory up
     * to date with no need to reload them all the time.
     */
    SINGLE,
    
    /**
     * Several processes access the database (for load balancing).
     * (Clients on hosts are run in multi-access mode.)
     */
    MULTI;
    
}
