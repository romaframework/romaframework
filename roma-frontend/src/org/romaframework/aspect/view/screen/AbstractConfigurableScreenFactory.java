/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.view.screen;

import org.romaframework.aspect.view.feature.ViewClassFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

public abstract class AbstractConfigurableScreenFactory implements ScreenFactory {

	public static XmlFormAreaAnnotation getScreenConfiguration(SchemaClass currentClass) {
		while (currentClass != null) {
			XmlFormAreaAnnotation rootArea = currentClass.getFeature(ViewClassFeatures.FORM);
			if (rootArea != null) {
				return rootArea;
			}
			currentClass = currentClass.getSuperClass();
		}

		// RETURN OBJECT FORM AREA
		return Roma.schema().getSchemaClass(Object.class).getFeature(ViewClassFeatures.FORM);
	}
}
