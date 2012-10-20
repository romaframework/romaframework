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

package org.romaframework.frontend.domain.crud;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.persistence.PersistenceException;
import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

/**
 * Like CRUDMain is an entrypoint for the CRUD but to select instances to be bound on calling page.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          ComposedEntity class used to display the result in the table.
 */
@CoreClass(orderActions = "select cancel search create read update delete report selectAll deselectAll")
public abstract class CRUDSelect<T> extends CRUDMain<T> implements Bindable {

	private SchemaField			sourceField;

	private Object					sourceObject;

	protected static Log		log								= LogFactory.getLog(CRUDSelect.class);

	public static final int	DEF_PAGE_ELEMENTS	= 5;

	public CRUDSelect(Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass, Class<? extends ComposedEntity<?>> iReadClass,
			Class<? extends ComposedEntity<?>> iEditClass) {
		this(null, iListClass, iCreateClass, iReadClass, iEditClass);
	}

	public CRUDSelect(GenericRepository<T> iRepository, Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass,
			Class<? extends ComposedEntity<?>> iReadClass, Class<? extends ComposedEntity<?>> iEditClass) {
		super(iRepository, iListClass, iCreateClass, iReadClass, iEditClass);
		setPageElements(DEF_PAGE_ELEMENTS);
	}

	public void setSource(Object iSourceObject, String iSourceFieldName) {
		if (iSourceFieldName != null && iSourceFieldName.contains(".")) {
			// GET NESTED SOURCE OBJECT
			sourceObject = SchemaHelper.getFieldObject(iSourceObject, iSourceFieldName);
		} else
			sourceObject = iSourceObject;

		// ASSIGN OBJECT SCHEMA FIELD
		SchemaClass cls = Roma.schema().getSchemaClass(iSourceObject);
		sourceField = cls.getField(iSourceFieldName);

		if (sourceField == null)
			throw new CRUDException("Cannot find field name " + iSourceObject.getClass().getSimpleName() + "." + iSourceFieldName + ". Check class definition");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return sourceObject;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return sourceField;
	}

	protected void onDoubleClick() {
		// DOUBLE CLICK: AUTO SELECT IT
		select();
	}

//	@Persistence(mode = PersistenceConstants.MODE_ATOMIC)
	public void select() {
		selectAndForceClosing(false);
	}

	public void selectAndForceClosing(boolean iForceWindowClosing) {
		Object[] fullSelection = getSelection();

		if (fullSelection != null)
			for (int i = 0; i < fullSelection.length; i++) {
				Object o;
				if (fullSelection[i] instanceof ComposedEntity<?>)
					o = ((ComposedEntity<?>) fullSelection[i]).getEntity();
				else
					o = fullSelection[i];
				fullSelection[i] = Roma.context().persistence().refreshObject(o, PersistenceAspect.FULL_MODE_LOADING, PersistenceAspect.STRATEGY_DETACHING);
				if (fullSelection[i] == null) {
					if (o != null)
						throw new PersistenceException("Error on detaching object " + o + ". Check if Class '" + o.getClass() + "' is detachable");
					else
						throw new PersistenceException("Error on detaching selected object.");
				}
			}

		SchemaHelper.insertElements(sourceField, sourceObject, fullSelection);

		Roma.aspect(FlowAspect.class).back();
	}

	public void cancel() {
		Roma.aspect(FlowAspect.class).back();
	}
}
