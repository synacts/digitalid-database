package net.digitalid.database.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.digitalid.database.core.configuration.Database;

/**
 * This annotation indicates that a method does not {@link Database#commit() commit} the current transaction.
 * In order to being able to roll back the whole method, actions and queries should never commit.
 * 
 * @see Committing
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface NonCommitting {}
