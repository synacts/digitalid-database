package net.digitalid.utility.database.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.digitalid.utility.database.configuration.Database;

/**
 * This annotation indicates that a method should only be called if the {@link Database database} is in {@link Database#isMultiAccess() multi-access} mode.
 * 
 * @see OnlyForSingleAccess
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface OnlyForMultiAccess {}
