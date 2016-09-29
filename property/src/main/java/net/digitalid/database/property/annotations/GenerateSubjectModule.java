package net.digitalid.database.property.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;

/**
 * This method interceptor generates a subject module with the name of the surrounding class and its converter.
 * 
 * @see GeneratePersistentProperty
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TODO(task = "Implement the method interceptor.", date = "2016-09-29", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.MIDDLE)
public @interface GenerateSubjectModule {}
