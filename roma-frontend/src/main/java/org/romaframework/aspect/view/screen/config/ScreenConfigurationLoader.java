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

import java.io.File;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.resource.AutoReloadListener;
import org.romaframework.core.resource.AutoReloadManager;
import org.romaframework.core.schema.SchemaClassResolver;

/**
 * Handle Desktop configuration descriptor loading.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ScreenConfigurationLoader extends Configurable<ScreenConfiguration> implements AutoReloadListener {
	public static final String	DEF_DESKTOP_FILE_NAME	= "main-screen.xml";
	private static Log					log										= LogFactory.getLog(ScreenConfigurationLoader.class);

	/**
	 * Get a Desktop Descriptor. Load if necessary and cache if for further accesses.
	 * 
	 * @param iName
	 * @return
	 */
	public ScreenConfiguration getDescriptor(String iName) {
		ScreenConfiguration descr;

		synchronized (this) {
			descr = getConfiguration(iName);
			if (descr == null) {
				// LOAD FROM DESCRIPTOR
				descr = loadDescriptor(iName);
				if (descr != null) {
					addConfiguration(iName, descr);
				}
			}
		}
		return descr;
	}

	/**
	 * Load XML descriptor using Apache XMLBeans. Entities are cached.
	 * 
	 * @param iEntityName
	 * @return Entity instance read from XML descriptor
	 */
	protected ScreenConfiguration loadDescriptor(String iEntityName) {
		ScreenConfiguration descr = null;
		String filePath = "<unsetted>";
		try {
			filePath = Roma.component(SchemaClassResolver.class).getClassDescriptorPath(iEntityName);

			if (filePath != null) {
				File location = null;
				try {
					try {
						location = new File(getClass().getResource(filePath).toURI());
					} catch (URISyntaxException ex) {
						location = new File(getClass().getResource(filePath).getPath());
					}
				} catch (Exception e) {
					log.warn("[DesktopConfigurationLoader.loadDescriptor] Cannot add resource " + filePath + " to reload manager.");
				}
				if (location != null)
					// REGISTER DESCRIPTOR FILE TO BE WAKED UP ON RELOADING
					Roma.component(AutoReloadManager.class).addResource(location, this);

				log.debug("[DesktopConfigurationLoader.loadDescriptor] Loading desktop from: " + filePath);
				descr = new ScreenConfiguration(getClass().getResourceAsStream(filePath));

			}
		} catch (Exception e) {
			log.error("[DesktopConfigurationLoader.loadDescriptor] Error on desktop resource: " + filePath, e);
		}
		return descr;
	}

	/**
	 * Reload desktop configuration from file. This event is invoked when the file descriptor is changed.
	 */
	public void signalUpdatedFile(File iFile) {
		synchronized (this) {
			ScreenConfiguration d = configuration.get(iFile.getName());
			if (d != null) {
				if (d.getFile().equals(iFile)) {
					log.warn("[DesktopConfigurationLoader.signalUpdatedFile] Reloading file: " + iFile);
					try {
						d.load();
					} catch (Exception e) {
						log.error("[DesktopConfigurationLoader.signalUpdatedFile] Error on loading updated descriptor: " + iFile.getAbsolutePath());
					}
				}
			}
		}
	}
}
