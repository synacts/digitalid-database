package net.digitalid.database.annotations.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

/**
 *
 */
@Deprecated // TODO: There is already another Default annotation!
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Default {
    
    @Nonnull String value();
}
