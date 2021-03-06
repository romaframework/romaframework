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

package org.romaframework.aspect.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewClass {

	String label() default AnnotationConstants.DEF_VALUE;

	String description() default AnnotationConstants.DEF_VALUE;

	/**
	 * Allows you to set the type of layout that will take the field,
	 * see <b>ViewConstants</b> the various opportunities
	 * @return
	 */
	String render() default AnnotationConstants.DEF_VALUE;

	String style() default AnnotationConstants.DEF_VALUE;

	AnnotationConstants explicitElements() default AnnotationConstants.UNSETTED;

	@Deprecated
	int columns() default 0;

	AnnotationConstants hideRequiredColumn() default AnnotationConstants.UNSETTED;

	/*
	 * Order of display areas. Names must be separated by spaces.
	 */
	@Deprecated
	String orderAreas() default AnnotationConstants.DEF_VALUE;

	String form() default AnnotationConstants.DEF_VALUE;

}
