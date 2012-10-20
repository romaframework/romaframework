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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.validation.annotation.ValidationAction;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewHelper;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.repository.PersistenceAspectRepositorySingleton;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.reporting.ReportGenerator;

/**
 * Page to handle Create, Modify and View sides of CRUD.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          Domain Object to handle
 */
@CoreClass(orderActions = "save cancel report")
@ViewClass(label = "")
public class CRUDInstance<T> extends CRUDEntity<T> implements ViewCallback, CRUDWorkingMode {

	@ViewField(visible = AnnotationConstants.FALSE)
	private int											mode;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected GenericRepository<T>	repository;

	public CRUDInstance() {
		super(null);
	}

	public CRUDInstance(T iEntity) {
		super(iEntity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onShow() {
		if (Roma.reporting() == null || mode == MODE_CREATE || mode == MODE_EMBEDDED)
			Roma.setFeature(this, "report", ViewActionFeatures.VISIBLE, Boolean.FALSE);

		if (mode == MODE_READ)
			// ENABLE/DISABLE ALL FIELDS DEPENDING ON MODE
			ViewHelper.enableFields(this, false);

		// HIDE/SHOW THE SAVE BUTTON
		Roma.setFeature(this, "save", ViewActionFeatures.VISIBLE, mode != MODE_READ && mode != MODE_EMBEDDED);

		// HIDE/SHOW THE CANCEL BUTTON
		Roma.setFeature(this, "cancel", ViewActionFeatures.VISIBLE, mode != MODE_EMBEDDED);

		if (mode != MODE_EMBEDDED && repository == null) {
			if (entity != null) {
				// TRY TO LOAD ITS REPOSITORY
				repository = (GenericRepository)Roma.repository((Class)entity.getClass());
			}

			if (repository == null)
				// LOAD THE DEFAULT REPOSITORY
				repository = (GenericRepository<T>) PersistenceAspectRepositorySingleton.getInstance();
		}
	}

	public void onDispose() {
	}

	/**
	 * Overwrite this method to catch the event before to display a CRUD instance in CREATE mode.
	 */
	public void onCreate() {
	}

	/**
	 * Overwrite this method to catch the event before to display a CRUD instance in READ mode.
	 */
	public void onRead() {
	}

	/**
	 * Overwrite this method to catch the event before to display a CRUD instance in UPDATE mode.
	 */
	public void onUpdate() {
	}

	@ValidationAction(validate = AnnotationConstants.TRUE)
	@Persistence(mode = PersistenceConstants.MODE_TX)
	public void save() {
		saveEntity();

		Object backForm = Roma.component(FlowAspect.class).back();

		if (backForm != null) {
			if (backForm instanceof CRUDSelect<?>) {
				// CALLER FORM IS A CRUD SELECT: SELECT THE CREATED ELEMENT AND
				// BIND INTO CALLER FORM (INVOKE SELECTION)
				((CRUDSelect<?>) backForm).setSelection(new Object[] { getEntity() });
				((CRUDSelect<?>) backForm).selectAndForceClosing(true);
				return;
			} else if (backForm instanceof Refreshable) {
				// RE-EXECUTE THE SEARCH
				((Refreshable) backForm).refresh();
			}
		}
	}

	@ValidationAction(validate = AnnotationConstants.TRUE)
	@ViewAction(visible = AnnotationConstants.FALSE)
	@Persistence(mode = PersistenceConstants.MODE_TX)
	public void apply() {
		saveEntity();
		setEntity(repository.load(entity, PersistenceAspect.FULL_MODE_LOADING, PersistenceAspect.STRATEGY_DETACHING));
	}

	protected void saveEntity() {
		switch (mode) {
		case MODE_CREATE:
			if (getSourceObject() == null)
				setEntity(repository.create(getEntity()));
			else
				SchemaHelper.insertElements(getSourceField(), getSourceObject(), new Object[] { entity });
			break;

		case MODE_UPDATE:
			if (getSourceObject() == null)
				setEntity(repository.update(getEntity()));
			else
				SchemaHelper.insertElements(getSourceField(), getSourceObject(), new Object[] { entity });
			break;

		case MODE_READ:
			return;
		}

	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;

		switch (mode) {
		case MODE_CREATE:
			onCreate();
			break;

		case MODE_UPDATE:
			onUpdate();
			break;

		case MODE_READ:
			onRead();
			return;
		}
	}

	/**
	 * Generate a report for the current view using the ReportingAspect.
	 */
	public void report() {
		Roma.component(FlowAspect.class).forward(new ReportGenerator(this, this.getClass().getSimpleName()));
	}

	public GenericRepository<T> getRepository() {
		return repository;
	}

	public void setRepository(GenericRepository<T> repository) {
		this.repository = repository;
	}

}
