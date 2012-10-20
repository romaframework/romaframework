/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.aspect.reporting.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.core.schema.FeatureNotSet;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportingClass {
	String documentType() default AnnotationConstants.DEF_VALUE;

	String label() default AnnotationConstants.DEF_VALUE;

	Class<?> entity() default FeatureNotSet.class;

	String render() default AnnotationConstants.DEF_VALUE;

	String layout() default AnnotationConstants.DEF_VALUE;

	AnnotationConstants explicitElements() default AnnotationConstants.UNSETTED;

	AnnotationConstants inheritViewConfiguration() default AnnotationConstants.UNSETTED;

}
