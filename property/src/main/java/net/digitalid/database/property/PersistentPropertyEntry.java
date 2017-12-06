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
package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.subject.Subject;
import net.digitalid.database.property.value.PersistentValuePropertyEntry;

/**
 * This class models an entry in the {@link PersistentPropertyTable persistent property table}.
 * 
 * @see PersistentValuePropertyEntry
 */
@Immutable
public abstract class PersistentPropertyEntry<@Unspecifiable SUBJECT extends Subject<?>> extends RootClass {
    
    /* -------------------------------------------------- Subject -------------------------------------------------- */
    
    /**
     * Returns the subject to which the property belongs.
     */
    @Pure
    @PrimaryKey
    public abstract @Nonnull SUBJECT getSubject();
    
}
