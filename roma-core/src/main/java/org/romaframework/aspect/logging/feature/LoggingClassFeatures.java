/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.logging.feature;

import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class LoggingClassFeatures {

	public static final Feature<String>		MODE		= new Feature<String>(LoggingAspect.ASPECT_NAME, "mode", FeatureType.CLASS, String.class);
	public static final Feature<Boolean>	ENABLED	= new Feature<Boolean>(LoggingAspect.ASPECT_NAME, "enabled", FeatureType.CLASS, Boolean.class);

}
