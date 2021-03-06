/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.schema;

import java.io.File;

/**
 * Listener interface to allow to get waked up when a schema class is reloaded.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface SchemaReloadListener {
	

	/**
	 * When update an SchemaClass remove all instances of this from all caches.
	 * 
	 * @param iClass
	 * @param iFile
	 */
	public void signalUpdatedClass(SchemaClass iClass, File iFile);
}
