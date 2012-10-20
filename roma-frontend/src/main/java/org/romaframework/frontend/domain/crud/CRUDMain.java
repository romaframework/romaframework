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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.logging.annotation.LoggingAction;
import org.romaframework.aspect.logging.annotation.LoggingClass;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.persistence.QueryByExample;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItem;
import org.romaframework.aspect.persistence.QueryByText;
import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.reporting.annotation.ReportingField;
import org.romaframework.aspect.security.Secure;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.form.SelectableInstance;
import org.romaframework.core.Roma;
import org.romaframework.core.classloader.ClassLoaderListener;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.repository.PersistenceAspectRepositorySingleton;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageOk;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.message.MessageText;
import org.romaframework.frontend.domain.message.MessageYesNo;
import org.romaframework.frontend.domain.reporting.ReportGenerator;
import org.romaframework.frontend.util.RomaCsvGenerator;

/**
 * Main class to display CRUD entry point. It allows to make queries using the filter on top of the page. Results are displayed on
 * bottom of the page along with management of multi-pages. You can change CRUD behavior and layout in the subclass or by Xml
 * Annotation.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          ComposedEntity class used to display the result in the table.
 */
@CoreClass(orderFields = "filter paging result", orderActions = "search create read update delete report selectAll deselectAll")
@LoggingClass(mode = LoggingConstants.MODE_DB)
@SuppressWarnings("unchecked")
public abstract class CRUDMain<T> extends SelectableInstance implements PagingListener, MessageResponseListener, Refreshable, ViewCallback {

	@ReportingField(visible = AnnotationConstants.FALSE)
	@ViewField(label = "", render = ViewConstants.RENDER_OBJECTEMBEDDED, position = "form://paging")
	protected CRUDPaging																	paging;

	protected org.romaframework.aspect.persistence.Query	queryRequest;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected GenericRepository<T>												repository;

	protected SchemaClass																	listClass;
	protected SchemaClass																	createClass;
	protected SchemaClass																	readClass;
	protected SchemaClass																	updateClass;

	protected static Log																	log										= LogFactory.getLog(CRUDMain.class);

	public static final String														SEARCH_MODE_LOADING		= "search";

	protected long																				lastSelectionTime			= 0;
	@ViewField(visible = AnnotationConstants.FALSE)
	protected boolean																			handleDoubleClick			= true;
	private static final int															DOUBLE_CLICK_TIMEOUT	= 1000;

	protected CRUDMain(Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass, Class<? extends ComposedEntity<?>> iReadClass,
			Class<? extends ComposedEntity<?>> iEditClass) {
		this(null, iListClass, iCreateClass, iReadClass, iEditClass);
	}

	protected CRUDMain(GenericRepository<T> iRepository, Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iCreateClass,
			Class<? extends ComposedEntity<?>> iReadClass, Class<? extends ComposedEntity<?>> iEditClass) {

		paging = new CRUDPaging(this);

		if (iRepository == null) {
			repository = Roma.repository(SchemaHelper.getSuperclassGenericType(this.getClass()));
			if (repository == null) {
				repository = (GenericRepository<T>) PersistenceAspectRepositorySingleton.getInstance();
			}
		} else {
			repository = iRepository;
		}

		listClass = Roma.schema().getSchemaClass(iListClass);
		createClass = Roma.schema().getSchemaClass(iCreateClass);
		readClass = Roma.schema().getSchemaClass(iReadClass);
		updateClass = Roma.schema().getSchemaClass(iEditClass);
	}

	@LoggingAction(enabled = AnnotationConstants.FALSE)
	public void onShow() {
		if (Roma.reporting() == null) {
			Roma.setFeature(this, "report", ViewActionFeatures.VISIBLE, Boolean.FALSE);
		}
	}

	@CoreField(embedded = AnnotationConstants.TRUE, useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(label = "", position = "form://filter")
	public abstract ComposedEntity<?> getFilter();

	@ViewField(label = "", selectionField = "selectionFromResult", render = ViewConstants.RENDER_TABLE, enabled = AnnotationConstants.FALSE, position = "form://result")
	public abstract List<? extends Object> getResult();

	public abstract void setResult(Object iValue);

	public void onDispose() {
		Controller.getInstance().unregisterListener(ClassLoaderListener.class, this);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		search();
	}

	public void loadPage(int iFrom, int iTo) {
		if (queryRequest == null) {
			return;
		}
		queryRequest.setRangeFrom(iFrom, iTo);
		executeQuery();
	}

	public CRUDPaging getPaging() {
		return paging;
	}

	/**
	 * Generate a report for the current view using the ReportingAspect.
	 */
	@ViewAction(visible = AnnotationConstants.TRUE)
	public void report() {
		ReportGenerator form = new ReportGenerator(this, this.getClass().getSimpleName());
		Roma.aspect(FlowAspect.class).forward(form);
	}

	/**
	 * Generate a report CSV for the current view using the RomaCsvGenerator.
	 */
	@ViewAction(visible = AnnotationConstants.TRUE)
	public void exportToCsv() {
		if (getSelection() != null && getSelection().length > 0) {
			RomaCsvGenerator.generateCsv(getSelection(), ((ComposedEntity<?>) getFilter()).getEntity().getClass().getSimpleName());
		} else {
			RomaCsvGenerator.generateCsv(getResult(), ((ComposedEntity<?>) getFilter()).getEntity().getClass().getSimpleName());
		}
	}

	/**
	 * By default search uses the "Search By Example" pattern by invoking the searchByExample() method. Overwrite this to use the
	 * searchByQuery() or any other mode.
	 */
	@Persistence(mode = PersistenceConstants.MODE_TX)
	@ViewAction(submit = AnnotationConstants.TRUE)
	public void search() {
		searchByExample();
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void showAll() {
		Class<?> entityClass = ((ComposedEntity<?>) getFilter()).getEntity().getClass();
		QueryByFilter addFilter = createAdditionalFilter(entityClass);
		queryRequest = addFilter;
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);

		executePagingQuery();
	}

	/**
	 * Search entities using the "Search By Example" pattern. All filter's properties with value different by null are evaluated in
	 * the query predicate.
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void searchByExample() {
		Object filter = ((ComposedEntity<?>) getFilter()).getEntity();
		QueryByFilter addFilter = createAdditionalFilter(filter.getClass());
		queryRequest = new QueryByExample(filter, addFilter);
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);

		executePagingQuery();
	}

	/**
	 * Search entities using the "Search By Example" pattern. All filter's properties with value different by null are evaluated in
	 * the query predicate. iAdditionalFilter allow to specify additional constaints for the autogenerated QBE.
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void searchByExample(QueryByFilter iAdditionalFilter) {
		Object filter = ((ComposedEntity<?>) getFilter()).getEntity();
		QueryByFilter addFilter = createAdditionalFilter(filter.getClass(), iAdditionalFilter);
		queryRequest = new QueryByExample(filter.getClass(), filter, addFilter);
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);

		executePagingQuery();
	}

	/**
	 * Search entities using the "Search By Example" pattern. This variant uses a QueryFilter instance as filter. This allow a much
	 * more powerful control over filtering by simple QBE upon.
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void searchByFilter(QueryByFilter iQueryFilter) {
		queryRequest = iQueryFilter;
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);
		executePagingQuery();
	}

	/**
	 * Search entities by passing a query text. The language of query text must be understood by underline Persistence Aspect.
	 * 
	 * @param iText
	 *          Query text in the language supported by underline Persistence Aspect
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void searchByText(String iText) {
		Object filter = ((ComposedEntity<?>) getFilter()).getEntity();
		queryRequest = new QueryByText(filter.getClass(), iText);
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);

		executePagingQuery();
	}

	/**
	 * Search entities using the "Search By Text" pattern. This method uses a QueryByText. This allow a much more powerful control
	 * over searchByQuery
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void searchByText(QueryByText iQueryText) {
		queryRequest = iQueryText;
		queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		queryRequest.setMode(SEARCH_MODE_LOADING);
		executePagingQuery();
	}

	protected void executePagingQuery() {
		if (paging != null)
			queryRequest.setRangeFrom(0, paging.getPageElements());
		int count = (int) repository.countByCriteria(queryRequest);
		executeQuery();

		if (paging != null) {
			paging.setTotalItems(count);
			paging.setCurrentPage(1);
			Roma.fieldChanged(this, "paging");
		}

		if (getResult().size() == 1) {
			setSelection(new Object[] { getResult().get(0) });
		} else {
			setSelection(null);
		}
	}

	/**
	 * Delegate the execution to the repository created and fill the result in own list.
	 */
	protected void executeQuery() {
		List<T> repositoryResult = repository.findByCriteria(queryRequest);
		fillResult(repositoryResult);
	}

	/*
	 * Fill CRUD result with the query result
	 */
	protected void fillResult(List<T> repositoryResult) {
		List<?> tempResult = repositoryResult;

		try {
			tempResult = EntityHelper.createComposedEntityList(repositoryResult, listClass);
		} catch (Exception e) {
			log.error("[CRUDMain.fillResult] Error on creating wrapper class for result. Class: " + listClass, e);
		}

		setResult(tempResult);
		Roma.fieldChanged(this, "result");
	}

	/**
	 * Create a new instance. It's the "C" of CRUD pattern.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
//	@Persistence(mode = PersistenceConstants.MODE_ATOMIC)
	@ViewAction(visible = AnnotationConstants.TRUE)
	public Object create() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return createInstance();
	}

	/**
	 * Read the selected instance. It's the "R" of CRUD pattern.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@ViewAction(visible = AnnotationConstants.TRUE)
//	@Persistence(mode = PersistenceConstants.MODE_ATOMIC)
	public Object read() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object selectedObj = getOnlyOneSelectedItem(getSelection());

		if (selectedObj == null) {
			return null;
		}

		Object loadedObject = loadObjectDetails(selectedObj);

		Object readInstance = CRUDHelper.getCRUDObject(readClass, loadedObject);

		if (readInstance instanceof CRUDInstance<?>) {
			((CRUDInstance<T>) readInstance).setRepository(repository);
			((CRUDInstance<?>) readInstance).setMode(CRUDInstance.MODE_READ);
		}

		displayInstanceForm(readInstance);
		return readInstance;
	}

	/**
	 * Update the selected instance. It's the "U" of CRUD pattern.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@ViewAction(visible = AnnotationConstants.TRUE)
//	@Persistence(mode = PersistenceConstants.MODE_ATOMIC)
	public Object update() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object selectedObj = getOnlyOneSelectedItem(getSelection());

		if (selectedObj == null) {
			return null;
		}

		Object loadedObject = loadObjectDetails(selectedObj);

		Object updateInstance = CRUDHelper.getCRUDObject(updateClass, loadedObject);

		if (updateInstance instanceof CRUDInstance<?>) {
			((CRUDInstance<T>) updateInstance).setRepository(repository);
			((CRUDInstance<?>) updateInstance).setMode(CRUDInstance.MODE_UPDATE);
		}

		displayInstanceForm(updateInstance);

		return updateInstance;
	}

	/**
	 * Display the form. Override this method to obtain finer control of where to display the form.
	 * 
	 * @param updateInstance
	 */
	protected void displayInstanceForm(Object updateInstance) {
		Roma.flow().forward(updateInstance);
	}

	protected Object loadObjectDetails(Object iObj) {
		Object obj = repository.load(((ComposedEntity<T>) iObj).getEntity(), PersistenceAspect.FULL_MODE_LOADING, PersistenceAspect.STRATEGY_DETACHING);

		if (obj == null) {
			throw new ConfigurationException("Cannot load object. Check the PersistenceAspect configuration and assure the class you're using is detachable");
		}

		return obj;
	}

	protected Object getOnlyOneSelectedItem(Object[] iSelection) {
		if (iSelection == null || iSelection.length != 1) {
			MessageOk dialog = new MessageOk("crud", "$Message.Information");
			dialog.setIcon("information.gif");
			String message = Roma.i18n().get("CRUDMain.selectOnlyOne.error");
			if (message == null || message.trim().length() == 0) {
				message = "Select one item";
			}
			dialog.setMessage(message);
			Roma.aspect(FlowAspect.class).popup(dialog);
			return null;
		}

		return iSelection[0];
	}

	/**
	 * Delete the current selection allowing the multi-selection of items. It's the "D" of CRUD pattern.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */

	@ViewAction(visible = AnnotationConstants.TRUE)
	public void delete() throws InstantiationException, IllegalAccessException {
		Object[] selection = getSelection();
		MessageText msg = null;
		if (selection == null || selection.length == 0) {
			msg = new MessageOk("delete", "Information", this);
			msg.setMessage("$CRUDMain.selectAtLeastOne.error");
			msg.setIcon("information.gif");
		} else {
			msg = new MessageYesNo("delete", "Warning", this);
			msg.setMessage("$CRUDMain.delete.confirm");
			msg.setIcon("question.gif");
		}

		Roma.aspect(FlowAspect.class).popup(msg);
	}

	public void loadAllPages() {
		queryRequest.setRangeFrom(0, queryRequest.getTotalItems());
		executeQuery();
		Roma.fieldChanged(this, "paging");
	}

	public void responseMessage(Message iMessage, Object iResponse) {
		if (iResponse == null || !((Boolean) iResponse)) {
			// USER SELECT NO
			return;
		}

		Object[] selection = getSelection();
		if (selection != null) {
			Object selectedInstance;
			ArrayList<Object> selectedInstances = new ArrayList<Object>();
			// BUILD THE ARRAY OF OBJECT TO DELETE
			for (int i = 0; i < selection.length; ++i) {
				selectedInstance = selection[i];

				if (selection[i] instanceof ComposedEntity<?>) {
					// GET THE COMPOSED SELECTION
					selectedInstance = ((ComposedEntity<?>) selectedInstance).getEntity();
				}

				selectedInstances.add(selectedInstance);
			}

			executeDelete(selectedInstances);

			// RE-EXECUTE THE QUERY TO UPDATE VIEW
			search();
		}
	}

	/**
	 * Execute the delete after the confirmation. Override this method if you want to handle the deletion by yourself. Useful in
	 * non-physical deletion.
	 * 
	 * @param selectedInstances
	 *          List of objects to delete
	 */
	protected void executeDelete(List<Object> selectedInstances) {
		// DELETE THE PERSISTENT OBJECTS
		repository.delete(selectedInstances.toArray());
	}

	/**
	 * Enable/Disable actions, based on current user profiling if any
	 */
	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@Override
	public void setSelection(Object[] iSelectedObjects) {
		if (iSelectedObjects == null || iSelectedObjects.length == 0) {
			// TEMPORARY PATCH TO GET WORKING CRUDS WITH THE NEW VIEW ASPECT
			Roma.setFeature(this, "delete", ViewActionFeatures.ENABLED, Boolean.FALSE);
		} else {
			Roma.setFeature(this, "delete", ViewActionFeatures.ENABLED, iSelectedObjects.length > 0);

			boolean enableUpdate = iSelectedObjects.length == 1;

			if (iSelectedObjects.length == 1 && iSelectedObjects[0] instanceof ComposedEntity<?>)
				enableUpdate = ((Secure) iSelectedObjects[0]).canWrite();

			// Roma.setFeature(this, "read", ViewActionFeatures.ENABLED, enableUpdate);
			// Roma.setFeature(this, "update", ViewActionFeatures.ENABLED, enableUpdate);
		}

		super.setSelection(iSelectedObjects);
		// Roma.fieldChanged(this, "result");
		lastSelectionTime = System.currentTimeMillis();
	}

	protected boolean checkDoubleClick(Object[] iSelectedObjects) {
		if (handleDoubleClick && iSelectedObjects != null && iSelectedObjects.length == 1 && getSelection() != null && getSelection().length == 1
				&& iSelectedObjects[0].equals(getSelection()[0]) && System.currentTimeMillis() - lastSelectionTime < DOUBLE_CLICK_TIMEOUT) {

			// DOUBLE CLICK:
			onDoubleClick();
			return true;
		}
		return false;
	}

	/**
	 * Overwrite this to change the behavior of double click on a selected element in the result.
	 */
	protected void onDoubleClick() {
		try {
			update();
		} catch (Exception e) {
			log.error("[CRUDMain.setSelection] Error on double click when calling the update method", e);
		}
	}

	public void selectAll() {
		Object[] sel = new Object[((Collection<?>) getResult()).size()];
		((Collection<?>) getResult()).toArray(sel);
		setSelection(sel);
		Roma.fieldChanged(this, "result");
	}

	public void deselectAll() {
		setSelection(null);
		Roma.fieldChanged(this, "result");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public int getPageElements() {
		if (paging == null)
			return 0;
		return paging.getPageElements();
	}

	public void setPageElements(int pageElements) {
		if (paging != null)
			paging.setPageElements(pageElements);
	}

	/**
	 * Method mantained to be compatible with old generated CRUDs
	 * 
	 * @param iObj
	 */
	public void setBackObject(Object iObj) {
	}

	public GenericRepository<T> getRepository() {
		return repository;
	}

	public boolean isHandleDoubleClick() {
		return handleDoubleClick;
	}

	public void setHandleDoubleClick(boolean updateOnDoubleClick) {
		this.handleDoubleClick = updateOnDoubleClick;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object[] getSelectionFromResult() {
		return getSelection();
	}

	public void setSelectionFromResult(Object[] iSelectedObjects) {
		if (!checkDoubleClick(iSelectedObjects))
			setSelection(iSelectedObjects);
	}

	protected PersistenceAspect getPersistenceAspect() {
		return Roma.context().persistence();
	}

	protected Object createInstance(Object... iArgs) throws InstantiationException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, SecurityException,
			NoSuchMethodException {
		SchemaClass entityClass = createClass.getField(ComposedEntity.NAME).getType().getSchemaClass();
		Object entityObject = SchemaHelper.createObject(entityClass, iArgs);

		Object createInstance = CRUDHelper.getCRUDObject(createClass, entityObject);

		if (createInstance instanceof CRUDInstance<?>) {
			((CRUDInstance<T>) createInstance).setRepository(repository);
			((CRUDInstance<?>) createInstance).setMode(CRUDInstance.MODE_CREATE);
		}

		displayInstanceForm(createInstance);
		return createInstance;
	}

	protected QueryByFilter createAdditionalFilter(Class<?> entityClass) {
		return createAdditionalFilter(entityClass, null);
	}

	protected QueryByFilter createAdditionalFilter(Class<?> entityClass, QueryByFilter extendedFilter) {
		if (extendedFilter == null) {
			extendedFilter = new QueryByFilter(entityClass);
		}
		if (getFilter() instanceof CRUDFilter<?>) {
			QueryByFilter additionalFilter = ((CRUDFilter<?>) getFilter()).getAdditionalFilter();
			if (additionalFilter != null) {
				extendedFilter.merge(additionalFilter);
			}
		}
		addDefaultOrder(extendedFilter);
		return extendedFilter;
	}

	protected void addDefaultOrder(QueryByFilter addFilter) {
		SchemaClass entityClass = (SchemaClass) listClass.getField(ComposedEntity.NAME).getType().getSchemaClass();
		Iterator<SchemaField> it = entityClass.getFieldIterator();
		while (it.hasNext()) {
			SchemaField sf = it.next();
			if (!SchemaHelper.isMultiValueObject(sf) && Roma.context().persistence().isFieldPersistent(sf))
				addFilter.addOrder(sf.getName());
		}
	}

	public void onResultSort(String field, String mode) {

		List<QueryByFilterItem> items = new ArrayList<QueryByFilterItem>(((QueryByExample) queryRequest).getAdditionalFilter().getItems());
		((QueryByExample) queryRequest).getAdditionalFilter().clear();
		((QueryByExample) queryRequest).getAdditionalFilter().setItems(items);
		((QueryByExample) queryRequest).getAdditionalFilter().addOrder(field, "true".equals(mode) ? QueryByFilter.ORDER_ASC : QueryByFilter.ORDER_DESC);
		executePagingQuery();
	}
}