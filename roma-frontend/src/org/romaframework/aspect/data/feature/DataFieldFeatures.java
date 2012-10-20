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

package org.romaframework.aspect.data.feature;

import org.romaframework.aspect.data.DataBindingAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class DataFieldFeatures {

	@SuppressWarnings("rawtypes")
	public static final Feature<Class>		REPOSITORY		= new Feature<Class>(DataBindingAspect.ASPECT_NAME, "repository", FeatureType.FIELD, Class.class);
	public static final Feature<String>		METHOD				= new Feature<String>(DataBindingAspect.ASPECT_NAME, "method", FeatureType.FIELD, String.class);
	public static final Feature<String[]>	SEARCH_FIELDS	= new Feature<String[]>(DataBindingAspect.ASPECT_NAME, "searchFields", FeatureType.FIELD, String[].class);
	public static final Feature<Long>			LIMIT					= new Feature<Long>(DataBindingAspect.ASPECT_NAME, "limit", FeatureType.FIELD, Long.class);

}