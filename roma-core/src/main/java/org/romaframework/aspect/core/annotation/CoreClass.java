/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.core.schema.FeatureNotSet;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreClass {
	public enum LOADING_MODE {
		LAZY, EARLY;
	}

	/**
	 * Entity type for Extension by composition.
	 * 
	 * @return
	 */
	Class<?> entity() default FeatureNotSet.class;

	/*
	 * Order of fields. Field names must be separated by spaces.
	 */
	String[] orderFields() default AnnotationConstants.DEF_VALUE;

	/*
	 * Order of action. Action names must be separated by spaces.
	 */
	String[] orderActions() default AnnotationConstants.DEF_VALUE;

	/*
	 * Loading mode. By default is LAZY. Declaring as EARLY means configure the class on startup.
	 */
	LOADING_MODE loading() default LOADING_MODE.LAZY;
}
