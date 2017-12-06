/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.property.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.generator.annotations.meta.Interceptor;
import net.digitalid.utility.generator.information.method.MethodInformation;
import net.digitalid.utility.generator.information.type.TypeInformation;
import net.digitalid.utility.generator.interceptor.MethodInterceptor;
import net.digitalid.utility.processing.utility.ProcessingUtility;
import net.digitalid.utility.processor.generator.JavaFileGenerator;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.property.subject.SubjectModule;
import net.digitalid.database.property.subject.SubjectModuleBuilder;

/**
 * This method interceptor generates a subject module with the name of the surrounding class and its converter.
 * 
 * @see GeneratePersistentProperty
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Interceptor(GenerateSubjectModule.Interceptor.class)
public @interface GenerateSubjectModule {
    
    /**
     * This class generates the interceptor for the surrounding annotation.
     */
    @Stateless
    public static class Interceptor extends MethodInterceptor {
        
        @Pure
        @Override
        protected @Nonnull @NonEmpty String getPrefix() {
            return "implemented";
        }
        
        @Pure
        @Override
        @TODO(task = "Clean up this method.", date = "2017-01-22", author = Author.KASPAR_ETTER)
        public void generateFieldsRequiredByMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull TypeInformation typeInformation) {
            final @Nonnull String subjectConverter;
            if (ProcessingUtility.isRawSubtype(typeInformation.getType(), Unit.class)) {
                subjectConverter = javaFileGenerator.importIfPossible("net.digitalid.core.unit.CoreUnitConverterBuilder") + ".withType" + Brackets.inRound(typeInformation.getName() + ".class") + ".build()";
            } else {
                subjectConverter = typeInformation.getSimpleNameOfGeneratedConverter() + ".INSTANCE";
            }
            
            final @Nonnull TypeMirror unitType = ((DeclaredType) method.getReturnType()).getTypeArguments().get(0);
            javaFileGenerator.addField("static final @" + javaFileGenerator.importIfPossible(Nonnull.class) + " " + javaFileGenerator.importIfPossible(SubjectModule.class) + Brackets.inPointy(javaFileGenerator.importIfPossible(unitType) + ", " + javaFileGenerator.importIfPossible(typeInformation.getType())) + " MODULE = " + javaFileGenerator.importIfPossible(SubjectModuleBuilder.class) + ".withSubjectTable" + Brackets.inRound(subjectConverter) + ".build()");
        }
        
        @Pure
        @Override
        protected void implementInterceptorMethod(@Nonnull JavaFileGenerator javaFileGenerator, @Nonnull MethodInformation method, @Nonnull String statement, @Nullable String resultVariable, @Nullable String defaultValue) {
            javaFileGenerator.addStatement("return MODULE");
        }
        
    }
    
}
