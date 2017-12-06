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
package net.digitalid.database.property.map;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.PersistentPropertyEntry;
import net.digitalid.database.property.subject.Subject;

/**
 * This class models an entry in the {@link PersistentMapPropertyTable persistent map property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentMapPropertyEntry<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable KEY, @Unspecifiable VALUE> extends PersistentPropertyEntry<SUBJECT> {
    
    /* -------------------------------------------------- Key -------------------------------------------------- */
    
    /**
     * Returns a key of the property.
     */
    @Pure
    @PrimaryKey
    public abstract KEY getKey();
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns a value of the property.
     */
    @Pure
    public abstract VALUE getValue();
    
}
