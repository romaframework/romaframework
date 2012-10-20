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

package org.romaframework.aspect.view.layout;

import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaField;

/**
 * Interface to define layout mode.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface LayoutMode<T> {
	/**
	 * Place an entire object.
	 * 
	 * @param iForm
	 * @param iComponent
	 * @param where
	 * @param iDesktop
	 * @return
	 * @throws InvalidLayoutMode
	 */
	public String layoutClass(ContentForm iForm, T iComponent, String where, Screen iDesktop) throws InvalidLayoutMode;

	/**
	 * Place a field in the form.
	 * 
	 * @param iField
	 * @param iForm
	 * @param iComponent
	 * @param iMode
	 * @param iFieldContent
	 *          TODO
	 * @return
	 * @throws InvalidLayoutMode
	 */
	public String layoutField(SchemaField iField, ContentForm iForm, T iComponent, String iMode, Object iFieldContent) throws InvalidLayoutMode;

	/**
	 * Place the action in the form.
	 * 
	 * @param iAction
	 * @param iForm
	 * @param iComponent
	 * @param iMode
	 * @return
	 * @throws InvalidLayoutMode
	 */
	public String layoutAction(SchemaAction iAction, ContentForm iForm, T iComponent, String iMode) throws InvalidLayoutMode;
}
