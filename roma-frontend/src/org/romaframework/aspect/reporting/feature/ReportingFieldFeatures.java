/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.reporting.feature;

import org.romaframework.aspect.reporting.ReportingAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class ReportingFieldFeatures {

	public static final Feature<Boolean>	VISIBLE	= new Feature<Boolean>(ReportingAspect.ASPECT_NAME, "visible", FeatureType.FIELD, Boolean.class);

	public static final Feature<String>		LABEL		= new Feature<String>(ReportingAspect.ASPECT_NAME, "label", FeatureType.FIELD, String.class);
	public static final Feature<String>		LAYOUT	= new Feature<String>(ReportingAspect.ASPECT_NAME, "layout", FeatureType.FIELD, String.class);
	public static final Feature<String>		RENDER	= new Feature<String>(ReportingAspect.ASPECT_NAME, "render", FeatureType.FIELD, String.class);

}
