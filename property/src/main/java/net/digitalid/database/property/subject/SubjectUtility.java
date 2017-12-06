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
package net.digitalid.database.property.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.property.PersistentProperty;

/**
 * This class exposes a non-public method of the {@link Subject} to the subclasses of {@link PersistentProperty}.
 */
@Utility
public abstract class SubjectUtility {
    
    /**
     * Adds the given property to the given subject.
     * <p>
     * <em>Important:</em> This method should only be called from packages with the prefix {@code net.digitalid.database.property}!
     */
    @PureWithSideEffects
    public static void add(@Nonnull Subject<?> subject, @Nonnull PersistentProperty<?, ?> property) {
        subject.add(property);
    }
    
}
