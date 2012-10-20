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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.logging.annotation.LoggingClass;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.repository.GenericRepository;

/**
 * Main class to display CRUD entry point. It allows to make queries using the filter on top of the page. Results are displayed on
 * bottom of the page along with management of multi-pages. You can change CRUD behavior and layout in the subclass or by Xml
 * Annotation.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          ComposedEntity class used to display the result in the table.
 */
@CoreClass(orderFields = "filter paging result detail", orderActions = "search create read update delete report selectAll deselectAll")
@LoggingClass(mode = LoggingConstants.MODE_DB)
public abstract class CRUDMainDetail<T> extends CRUDMain<T> implements CRUDWorkingMode {

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	protected CRUDInstance<T>	detail;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected int							mode	= CRUDWorkingMode.MODE_UPDATE;

	public CRUDMainDetail(Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass, Class<? extends ComposedEntity<?>> iReadClass,
			Class<? extends ComposedEntity<?>> iEditClass) {
		super(iListClass, iCreateClass, iReadClass, iEditClass);
		// TODO Auto-generated constructor stub
	}

	protected CRUDMainDetail(GenericRepository<T> iRepository, Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass,
			Class<? extends ComposedEntity<?>> iReadClass, Class<? extends ComposedEntity<?>> iEditClass) {
		super(iRepository, iListClass, iCreateClass, iReadClass, iEditClass);
		// TODO Auto-generated constructor stub
	}

	public CRUDInstance<T> getDetail() {
		return detail;
	}

	protected void setDetail(CRUDInstance<T> iDetail) {
		detail = iDetail;
		Roma.fieldChanged(this, "detail");
	}

	@Override
	public void setSelection(Object[] selectedObjects) {
		if (selectedObjects == null || selectedObjects.length == 0) {
			setDetail(null);
			return;
		}

		try {
			switch (mode) {
			case CRUDWorkingMode.MODE_UPDATE:
				update();
				break;

			default:
				read();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.setSelection(selectedObjects);
	}

	@SuppressWarnings("unchecked")
	protected void displayInstanceForm(Object iDetail) {
		setDetail((CRUDInstance<T>) iDetail);
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
