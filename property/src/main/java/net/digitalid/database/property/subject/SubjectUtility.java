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
