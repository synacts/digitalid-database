package net.digitalid.database.annotations.transaction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a method ends in a committed state if no exception is thrown.
 * Otherwise, the current transaction has to be rolled back by the caller of the method (in case there is one).
 * 
 * @see NonCommitting
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Committing {}
