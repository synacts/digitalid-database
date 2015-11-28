package net.digitalid.database.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.digitalid.database.core.configuration.Database;

/**
 * This annotation indicates that a method may only be called when the {@link Database database} is {@link Database#isLocked() locked}.
 * 
 * @see NonLocked
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Locked {}
