package net.digitalid.database.annotations.unit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

@Deprecated // TODO: There is now a @GenerateTableConverter annotation that covers this functionality.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UnitName {
    
    public @Nonnull String value();
    
}
