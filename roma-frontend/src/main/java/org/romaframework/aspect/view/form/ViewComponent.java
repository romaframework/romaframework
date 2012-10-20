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

package org.romaframework.aspect.view.form;

import org.romaframework.core.config.Destroyable;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

/**
 * Base component interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface ViewComponent extends Destroyable{

	public String getScreenArea();

	public void setScreenArea(String area);

	public ViewComponent getContainerComponent();
	
	public void setContent(Object iContent);

	public Object getContent();

	public void setSchemaObject(SchemaObject iSchemaObject);

	public SchemaObject getSchemaObject();

	public void setSchemaField(SchemaField iSchemaField);

	public SchemaField getSchemaField();

	public Object getFieldComponent(String iName);
}
