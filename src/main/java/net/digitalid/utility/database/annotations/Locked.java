package net.digitalid.utility.database.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.digitalid.utility.database.configuration.Database;

/**
 * This annotation indicates that a method may only be called when the {@link Database database} is {@link Database#isLocked() locked}.
 * 
 * @see NonLocked
 * 
 * @author Kaspar Etter (kaspar.etter@digitalid.net)
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Locked {}
