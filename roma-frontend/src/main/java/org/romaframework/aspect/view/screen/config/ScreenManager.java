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
package org.romaframework.aspect.view.screen.config;

import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.aspect.view.screen.ScreenFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

/**
 * Manage Desktop instances. It acts as Desktop factory and cache of desktop original instances. When a Desktop is requested, then a
 * Desktop clone is created starting from original. This prevent to reload and parse Xml descriptor.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class ScreenManager extends Configurable<ScreenFactory> {

	public abstract Screen createScreenFromDefaultFactory(String iName, XmlFormAreaAnnotation iAreaTag, String defaultArea);

	public Screen getScreen(String iTypeName) {
		Screen screen = null;

		if (iTypeName.endsWith(SchemaClassResolver.DESCRIPTOR_SUFFIX))
			iTypeName = iTypeName.substring(0, iTypeName.length() - SchemaClassResolver.DESCRIPTOR_SUFFIX.length());

		// TRY TO GET DESKTOP FACTORY IN MEMORY
		ScreenFactory factory = getConfiguration(iTypeName);

		if (factory != null) {
			screen = factory.create(iTypeName);
		} else {
			synchronized (this) {
				// NO FACTORY DEFINED: CREATE A CONFIGURABLE DESKTOP LOADED FROM
				// DESCRIPTOR
				ScreenConfiguration descr = Roma.component(ScreenConfigurationLoader.class).getDescriptor(iTypeName);

				if (descr == null)
					// DESKTOP DESCRIPTOR NOT FOUND
					throw new ConfigurationException("Screen " + iTypeName + " not found");

				screen = createScreenFromDefaultFactory(iTypeName, descr.getRootArea(), descr.getDefaultArea());
			}
		}

		return screen;
	}
}
