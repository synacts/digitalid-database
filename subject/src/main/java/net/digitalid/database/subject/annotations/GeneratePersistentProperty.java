package net.digitalid.database.subject.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.meta.Interceptor;
import net.digitalid.utility.generator.information.method.MethodInformation;
import net.digitalid.utility.generator.information.type.TypeInformation;
import net.digitalid.utility.generator.interceptor.MethodInterceptor;
import net.digitalid.utility.processing.logging.ProcessingLog;
import net.digitalid.utility.processing.utility.ProcessingUtility;
import net.digitalid.utility.processing.utility.StaticProcessingEnvironment;
import net.digitalid.utility.processor.generator.JavaFileGenerator;
import net.digitalid.utility.string.Strings;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Stateless;

/**
 * This method interceptor generates a persistent property with the corresponding property table.
 * 
 * @see GenerateSubjectModule
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Interceptor(GeneratePersistentProperty.Interceptor.class)
@TODO(task = "Implement the usage checks.", date = "2016-12-09", author = Author.KASPAR_ETTER)
public @interface GeneratePersistentProperty {
    
    /**
     * This class generates the interceptor for the surrounding annotation.
     */
    @Stateless
    public static class Interceptor extends MethodInterceptor {
        
        @Pure
        @Override
        protected @Nonnull String getPrefix() {
            return "implemented";
        }
        
        @Pure
        @Override
        @TODO(task = "Implement the value validation part as well!", date = "2016-12-09", author = Author.KASPAR_ETTER)
        public void generateFieldsRequiredByMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull TypeInformation typeInformation) {
//            final @Nonnull FreezableArrayList<@Nonnull Contract> contracts = FreezableArrayList.withNoElements();
//            
//            final @Nullable TypeMirror componentType = ProcessingUtility.getComponentType(method.getReturnType());
//            final @Nullable TypeElement typeElement = ProcessingUtility.getTypeElement(componentType);
//            final @Modifiable @Nonnull Map<@Nonnull AnnotationMirror, @Nonnull ValueAnnotationValidator> valueValidators = AnnotationHandlerUtility.getValueValidators(typeElement);
//            for (Map.@Nonnull Entry<@Nonnull AnnotationMirror, @Nonnull ValueAnnotationValidator> valueValidatorEntry : valueValidators.entrySet()) {
//                final @Nullable Contract contract = valueValidatorEntry.getValue().generateContract(typeElement, valueValidatorEntry.getKey(), javaFileGenerator);
//                contracts.add(contract);
//            }
//            
//            final @Nonnull String validationContent = contracts.map(contract -> javaFileGenerator.importIfPossible(Require.class) + ".that" + Brackets.inRound(contract.getCondition()) + ".orThrow" + Brackets.inRound(contract.getMessage() + ", " + contract.getArguments().join()) + ";").join("\n");
//            
//            javaFileGenerator.addField("private static final @" + javaFileGenerator.importIfPossible(Nonnull.class) + " " + javaFileGenerator.importIfPossible(FailableConsumer.class) + Brackets.inPointy(javaFileGenerator.importIfPossible(String.class) + ", " + javaFileGenerator.importIfPossible(PreconditionViolationException.class)) + method.getName().toUpperCase() + "_VALIDATOR = new " + javaFileGenerator.importIfPossible(FailableConsumer.class) + Brackets.inPointy(javaFileGenerator.importIfPossible(String.class) + ", " + javaFileGenerator.importIfPossible(PreconditionViolationException.class)) + "() {\n\n " +
//                    
//                    "@" + javaFileGenerator.importIfPossible(Impure.class) + "\n" +
//                    "@" + javaFileGenerator.importIfPossible(Override.class) + "\n" +
//                    "public void consume(@" + javaFileGenerator.importIfPossible(Captured.class) + javaFileGenerator.importIfPossible(String.class) + " password) throws " + javaFileGenerator.importIfPossible(PreconditionViolationException.class) + " {\n" + validationContent + "\n}" +
//            "}\n}");
            
            // TODO: Clean up the following mess!
            
            final @Nonnull String upperCasePropertyName = method.getName().toUpperCase();
            final @Nonnull String propertyPackage = ProcessingUtility.getQualifiedPackageName(((DeclaredType) method.getReturnType()).asElement());
            final @Nonnull String propertyType = "Persistent" + Strings.substringFromLast(ProcessingUtility.getSimpleName(method.getReturnType()), "Persistent");
            
            final @Nonnull String surroundingType = typeInformation.getName();
            final @Nonnull List<@Nonnull ? extends TypeMirror> typeArguments = ((DeclaredType) method.getReturnType()).getTypeArguments();
            final @Nonnull String valueType = javaFileGenerator.importIfPossible(typeArguments.get(1));
            
            // TODO: The following code does not yet work for value converters that do not yet exist (i.e. will be generated in the same round).
            final @Nonnull String valueConverterName = ProcessingUtility.getQualifiedName(typeArguments.get(1)) + "Converter";
            final @Nullable TypeElement valueConverterElement = StaticProcessingEnvironment.getElementUtils().getTypeElement(valueConverterName);
            if (valueConverterElement == null) { ProcessingLog.warning("No type element was found for $, which might be because that type will only be generated in this round.", valueConverterElement); }
            final @Nullable DeclaredType valueConverter = valueConverterElement == null ? null : ProcessingUtility.getSupertype((DeclaredType) valueConverterElement.asType(), Converter.class);
            
            final @Nonnull String externallyProvidedType;
            if (valueType.endsWith("Role")) { // TODO: This hardcoding is only temporary, of course, and should be replaced as soon as the type information can be cached and retrieved.
                externallyProvidedType = javaFileGenerator.importIfPossible("net.digitalid.core.client.Client");
            } else if (valueConverter != null) {
                externallyProvidedType = javaFileGenerator.importIfPossible(valueConverter.getTypeArguments().get(1));
            } else {
                externallyProvidedType = "Void";
            }
            
            final @Nonnull String converter = (valueType.equals("String") ? javaFileGenerator.importIfPossible("net.digitalid.utility.conversion.converters.StringConverter") : javaFileGenerator.importIfPossible(ProcessingUtility.getQualifiedName(typeArguments.get(1)) + "Converter")) + ".INSTANCE";
            
            javaFileGenerator.addField("/* TODO: private */ static final @" + javaFileGenerator.importIfPossible(Nonnull.class) + " " + javaFileGenerator.importIfPossible(propertyPackage + "." + propertyType.replace("Simple", "") + "Table") + Brackets.inPointy(surroundingType + ", " + valueType + ", " + externallyProvidedType) + " " + upperCasePropertyName + "_TABLE = " + javaFileGenerator.importIfPossible(propertyPackage + "." + propertyType.replace("Simple", "") + "TableBuilder") + "." + Brackets.inPointy(surroundingType + ", " + valueType + ", " + externallyProvidedType) + "withName" + Brackets.inRound(Quotes.inDouble(method.getName())) + ".withParentModule(MODULE).withValueConverter" + Brackets.inRound(converter) + (propertyType.contains("Value") ? ".withDefaultValue" + Brackets.inRound(method.hasAnnotation(Default.class) ? method.getAnnotation(Default.class).value() : "null") : "") + ".build()");
            
            javaFileGenerator.addField("private final @" + javaFileGenerator.importIfPossible(Nonnull.class) + " " + javaFileGenerator.importIfPossible(propertyPackage + ".Writable" + propertyType) + Brackets.inPointy(surroundingType + ", " + valueType) + " " + method.getName() + " = " + javaFileGenerator.importIfPossible(propertyPackage + ".Writable" + propertyType + "Builder") + "." + Brackets.inPointy(surroundingType + ", " + valueType) + "withSubject(this).withTable" + Brackets.inRound(upperCasePropertyName + "_TABLE") + ".build()");
        }
        
        @Pure
        @Override
        protected void implementInterceptorMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull String statement, @Nullable String resultVariable, @Nullable String defaultValue) {
            javaFileGenerator.addStatement("return " + method.getName());
        }
        
    }
    
}
