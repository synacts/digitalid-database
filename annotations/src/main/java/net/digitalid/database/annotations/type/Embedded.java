package net.digitalid.database.annotations.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see Referenced
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, /* TODO: Probably remove the type use again once the annotation is used on the type declaration instead. */ ElementType.TYPE_USE})
public @interface Embedded {}
