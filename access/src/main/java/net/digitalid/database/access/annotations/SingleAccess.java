package net.digitalid.database.access.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.processing.utility.TypeImporter;
import net.digitalid.utility.validation.annotations.meta.MethodValidator;
import net.digitalid.utility.validation.annotations.type.Stateless;
import net.digitalid.utility.validation.contract.Contract;
import net.digitalid.utility.validation.validator.MethodAnnotationValidator;

import net.digitalid.database.access.Access;
import net.digitalid.database.access.Mode;

/**
 * This annotation indicates that a method should only be called if the database is in single-access mode.
 * 
 * @see MultiAccess 
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@MethodValidator(SingleAccess.Validator.class)
public @interface SingleAccess {
    
    /* -------------------------------------------------- Validator -------------------------------------------------- */
    
    /**
     * This class checks the use of and generates the contract for the surrounding annotation.
     */
    @Stateless
    public static class Validator implements MethodAnnotationValidator {
        
        @Pure
        @Override
        public @Nonnull Contract generateContract(@Nonnull Element element, @Nonnull AnnotationMirror annotationMirror, @NonCaptured @Modified @Nonnull TypeImporter typeImporter) {
            return Contract.with(typeImporter.importIfPossible(Access.class) + ".mode.get() == " + typeImporter.importIfPossible(Mode.class) + ".SINGLE", "The method " + Quotes.inSingle(element.getSimpleName().toString()) + " may only be called in single-access mode.");
        }
        
    }
    
}
