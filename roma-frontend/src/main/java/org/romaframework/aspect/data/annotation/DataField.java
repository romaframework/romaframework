/*
 *
 * Copyright 2010 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.aspect.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation for methods to use to describe automatic field binding.
 * 
 * @author luca.molino
 * 
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {

	/**
	 * Repository class that
	 * 
	 * @return
	 */
	Class<?> repository() default Object.class;

	/**
	 * Method to call in repository
	 * 
	 * @return
	 */
	String method() default "getAll";

	/**
	 * Filter fields used by the repository
	 * 
	 * @return
	 */
	String[] searchFields() default {};

	/**
	 * Result limit
	 * 
	 * @return
	 */
	int limit() default 0;

}
