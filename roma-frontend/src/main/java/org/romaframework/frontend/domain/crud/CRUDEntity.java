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

package org.romaframework.frontend.domain.crud;

import java.util.List;

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
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.page.EntityPage;

/**
 * Base class to handle CRUDInstances.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          Domain Object to handle
 */
@CoreClass(orderActions = "cancel")
public class CRUDEntity<T> extends EntityPage<T> implements Bindable {
	protected SchemaField	sourceField;
	protected Object			sourceObject;

	/**
	 * Create a CRUDEntity base object.
	 * 
	 * @param iEntity
	 *          Inherited entity
	 * @param iBackObject
	 *          The object to call on back action
	 */
	public CRUDEntity(T iEntity) {
		super(iEntity);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Persistence(mode = PersistenceConstants.MODE_ATOMIC)
	public void cancel() {
		Object backObj = Roma.aspect(FlowAspect.class).back();

		if (backObj != null && backObj instanceof CRUDMain<?>) {
			// REPLACE OBJECT IN THE SOURCE COLLECTION
			List<ComposedEntity<T>> result = ((CRUDMain) backObj).getResult();
			int pos = -1;

			// SEARCH THE OBJECT INSIDE THE CONTAINER INSTANCE
			for (int i = 0; i < result.size(); ++i) {
				if (result.get(i).getEntity().equals(getEntity())) {
					// FOUND!
					pos = i;
					break;
				}
			}

			if (pos != -1) {
				try {
					PersistenceAspect db = Roma.context().persistence();
					T reloadedObj = db.refreshObject(getEntity(), null, PersistenceAspect.STRATEGY_DETACHING);

					if (reloadedObj != null)
						result.get(pos).setEntity(reloadedObj);
				} catch (PersistenceException e) {
				}
			}
		}
	}

	public void setSource(Object iSourceObject, String iSourceFieldName) {
		if (iSourceFieldName != null && iSourceFieldName.contains(".")) {
			// GET NESTED SOURCE OBJECT
			sourceObject = SchemaHelper.getFieldObject(iSourceObject, iSourceFieldName);
		} else
			sourceObject = iSourceObject;

		if (iSourceObject != null) {
			// ASSIGN OBJECT SCHEMA FIELD
			SchemaClass cls = Roma.schema().getSchemaClass(iSourceObject.getClass());
			sourceField = cls.getField(iSourceFieldName);
		}
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return sourceObject;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return sourceField;
	}

}
