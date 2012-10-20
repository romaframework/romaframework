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
package org.romaframework.aspect.geo;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaField;

public interface GeoAspect extends Aspect {

	/**
	 * Return the Html of the map
	 * 
	 * @param value
	 *          the value where the map must be centered
	 * @param parameters
	 *          the list of parameters
	 * @return
	 */
	public String generateMap(Object pojo, SchemaField schemaField);

}
