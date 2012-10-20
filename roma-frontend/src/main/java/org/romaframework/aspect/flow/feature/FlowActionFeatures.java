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

package org.romaframework.aspect.flow.feature;

import org.romaframework.aspect.flow.FlowAspectAbstract;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaClass;

public class FlowActionFeatures {

	public static final Feature<SchemaClass>	NEXT							= new Feature<SchemaClass>(FlowAspectAbstract.ASPECT_NAME, "next", FeatureType.ACTION, SchemaClass.class);
	public static final Feature<String>				POSITION					= new Feature<String>(FlowAspectAbstract.ASPECT_NAME, "position", FeatureType.ACTION, String.class);
	public static final Feature<String>				ERROR							= new Feature<String>(FlowAspectAbstract.ASPECT_NAME, "error", FeatureType.ACTION, String.class);
	public static final Feature<Boolean>			BACK							= new Feature<Boolean>(FlowAspectAbstract.ASPECT_NAME, "back", FeatureType.ACTION, Boolean.class);

	public static final Feature<Boolean>			CONFIRM_REQUIRED	= new Feature<Boolean>(FlowAspectAbstract.ASPECT_NAME, "confirmRequired", FeatureType.ACTION, Boolean.class);
	public static final Feature<String>				CONFIRM_MESSAGE		= new Feature<String>(FlowAspectAbstract.ASPECT_NAME, "confirmMessage", FeatureType.ACTION, String.class);

}
