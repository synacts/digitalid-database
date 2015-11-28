package net.digitalid.database.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.digitalid.database.core.configuration.Database;

/**
 * This annotation indicates that a method should only be invoked on the {@link Database#isMainThread() main thread}.
 * It is safe to assume that the {@link Database} is {@link Database#isLocked() locked} for methods on the main thread.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface OnMainThread {}
