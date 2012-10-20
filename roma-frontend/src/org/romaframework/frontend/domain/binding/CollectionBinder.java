/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--assetdata.it)
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

package org.romaframework.frontend.domain.binding;

import java.util.Collection;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDException;
import org.romaframework.frontend.domain.page.Page;

/**
 * @author Luca Molino
 * 
 */
public class CollectionBinder<T> extends Page implements Bindable {

	private Object				sourceObject;

	private SchemaField		sourceField;

	@ViewField(label = "", render = ViewConstants.RENDER_SELECT, selectionField = "selection")
	private Collection<T>	collection;

	@ViewField(visible = AnnotationConstants.FALSE)
	private T[]						selection;

	public CollectionBinder() {
	}

	public CollectionBinder(Collection<T> iCollection) {
		collection = iCollection;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return sourceField;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return sourceObject;
	}

	public void setSource(Object iSourceObject, String iSourceFieldName) {
		sourceObject = iSourceObject;

		// ASSIGN OBJECT SCHEMA FIELD
		SchemaClass cls = Roma.schema().getSchemaClass(iSourceObject.getClass());
		sourceField = cls.getField(iSourceFieldName);

		if (sourceField == null)
			throw new CRUDException("Cannot find field name " + iSourceObject.getClass().getSimpleName() + "." + iSourceFieldName + ". Check class definition");
	}

	public Collection<T> getCollection() {
		return collection;
	}

	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	public T[] getSelection() {
		return selection;
	}

	public void setSelection(T[] selection) {
		this.selection = selection;
	}

	public void add() {
		addAndForceClosing(false);
	}

	public void addAndForceClosing(boolean iForceClosign) {
		SchemaHelper.insertElements(sourceField, sourceObject, selection);
		Roma.aspect(FlowAspect.class).back();
	}

}
