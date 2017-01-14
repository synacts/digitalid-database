package net.digitalid.database.annotations.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.digitalid.utility.validation.annotations.meta.ValueValidator;
import net.digitalid.utility.validation.validators.StringValidator;

/**
 * This annotation indicates that the annotated string is an SQL statement.
 * 
 * @see SQLFraction
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValueValidator(StringValidator.class)
@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface SQLStatement {}
