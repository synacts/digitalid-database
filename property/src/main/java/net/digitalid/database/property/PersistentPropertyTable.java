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
import net.digitalid.utility.storage.Table;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.map.PersistentMapPropertyTable;
import net.digitalid.database.property.set.PersistentSetPropertyTable;
import net.digitalid.database.property.subject.Subject;
import net.digitalid.database.property.subject.SubjectModule;
import net.digitalid.database.property.value.PersistentValuePropertyTable;

/**
 * A persistent property table belongs to a {@link SubjectModule subject module} and stores the {@link PersistentPropertyEntry property entries}.
 * 
 * @see PersistentMapPropertyTable
 * @see PersistentSetPropertyTable
 * @see PersistentValuePropertyTable
 */
@Immutable
public interface PersistentPropertyTable<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable ENTRY extends PersistentPropertyEntry<SUBJECT>> extends Table<ENTRY, @Nonnull UNIT> {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName();
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SubjectModule<UNIT, SUBJECT> getParentModule();
    
}
