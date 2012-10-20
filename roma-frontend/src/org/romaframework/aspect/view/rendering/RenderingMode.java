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

package org.romaframework.aspect.view.rendering;

import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaField;

/**
 * Interface to define rendering mode.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface RenderingMode<T> {
	/**
	 * Return the class of component managed.
	 * 
	 * @return
	 */
	public Class<? extends T>[] getComponentClasses();

	/**
	 * Create a component to render for a class.
	 * 
	 * @param iForm
	 *          Parent form object
	 * @param iFormToRender
	 *          Form object containing the component where to render it.
	 * @return The component to render
	 * @throws InvalidRenderingMode
	 */
	public T renderClass(ViewComponent iForm, ViewComponent iFormToRender) throws InvalidRenderingMode;

	/**
	 * Create a component to render for a field.
	 * 
	 * @param iForm
	 *          Form object containing the component where to render it.
	 * @param iField
	 *          SchemaField representing the field to render
	 * @return The component to render
	 * @throws InvalidRenderingMode
	 */
	public T renderField(ViewComponent iForm, SchemaField iField, ViewComponent iFormToRender) throws InvalidRenderingMode;

	/**
	 * Create a component to render for an action.
	 * 
	 * @param iForm
	 *          Form object containing the component where to render it.
	 * @param iAction
	 *          SchemaAction representing the action to render
	 * @return The component to render
	 * @throws InvalidRenderingMode
	 */
	public T renderAction(ViewComponent iForm, SchemaAction iAction) throws InvalidRenderingMode;

	/**
	 * Get the component's content.
	 * 
	 * @param iComponent
	 *          Component object
	 * @param iValue
	 *          TODO
	 * @return The value if any, otherwise null
	 */
	public Object getContent(T iComponent, Object iValue);

	/**
	 * Set the component's value.
	 * 
	 * @param iComponent
	 *          Component object
	 * @param iValue
	 *          Value to set
	 */
	public void setContent(T iComponent, Object iValue);

	/**
	 * Refresh the component's value.
	 * 
	 * @param iComponent
	 *          Component object
	 * @param iValue
	 *          Value to set
	 */
	public void refresh(T iComponent, Object iValue);
}
