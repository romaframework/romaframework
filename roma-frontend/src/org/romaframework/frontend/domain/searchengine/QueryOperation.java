/*
 *
 * Copyright 2009 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.frontend.domain.searchengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.frontend.domain.message.MessageOk;
import org.romaframework.frontend.domain.message.MessageText;
import org.romaframework.frontend.domain.searchengine.filter.AbstractBaseFilter;
import org.romaframework.frontend.domain.searchengine.filter.BaseFilter;

/**
 * @author molino
 * 
 */
public class QueryOperation implements ViewCallback {

	@ViewField(render = ViewConstants.RENDER_SELECT, position = "form://availableFilters", selectionField = "selection", label = "")
	protected Map<String, String>	filters				= new HashMap<String, String>();

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String							selection;

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_COLSET, label = "", style = "queryOperation")
	protected List<QueryItem>			operation			= new ArrayList<QueryItem>();

	@ViewField(visible = AnnotationConstants.FALSE)
	protected BaseFilter<?>				editCondition;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Integer							positionToAdd	= 0;

	private final static Log			logger				= LogFactory.getLog(QueryOperation.class);

	public Set<String> executeQuery(PersistenceAspect db) {
		Set<String> result = null;
		String operationMode = null;
		if (operation.size() == 0)
			return null;
		for (int i = 0; i < operation.size(); i++) {
			QueryItem item = operation.get(i);
			if (item instanceof QueryOperator) {
				operationMode = ((QueryOperator) item).getOperator();
				continue;
			} else if (item instanceof QuerySubOperationDelimiter) {
				QuerySubOperationDelimiter start = (QuerySubOperationDelimiter) item;
				QuerySubOperationDelimiter end = start.getConnectedDelimiter();
				QueryOperation subOperation = new QueryOperation();
				subOperation.setOperation(operation.subList(start.getPosition() + 1, end.getPosition()));
				result = updateResult(result, operationMode, subOperation.executeQuery(db));
				i = end.getPosition();
			} else {
				result = updateResult(result, operationMode, ((QueryCondition) operation.get(i)).getEntity().executeQuery(db,null));
			}
		}
		return result;
	}

	public void onShow() {
		loadFilters();
		Roma.fieldChanged(this, "filters");

		if (operation.size() > 0)
			showFilters();
	}

	public void onDispose() {
	}

	public Set<String> getFilters() {
		return filters.keySet();
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public List<QueryItem> getOperation() {
		return operation;
	}

	public void setOperation(List<QueryItem> operation) {
		this.operation = operation;
	}

	@CoreField(useRuntimeType = AnnotationConstants.TRUE, expand = AnnotationConstants.TRUE)
	public BaseFilter<?> getEditCondition() {
		return editCondition;
	}

	public void setEditCondition(BaseFilter<?> editCondition) {
		this.editCondition = editCondition;
	}

	public void setPositionToAdd(Integer positionToAdd) {
		this.positionToAdd = positionToAdd;
	}

	@ViewAction(position = "form://availableFilters", style = "queryOperationAddOperator")
	public void add() throws ClassNotFoundException {
		if (selection == null) {
			showSelectOnlyOneError();
		} else if (selection.equals(Roma.i18n().get("QuerySubOperation.filter.label"))) {
			if (positionToAdd != null)
				addSubOperation(positionToAdd);
			hideFilters();
		} else {
			if (positionToAdd != null)
				add(positionToAdd);
			positionToAdd = null;
			hideFilters();
		}
	}

	protected void add(Integer position) throws ClassNotFoundException {
		BaseFilter<?> selectionInstance;
		try {
			selectionInstance = (BaseFilter<?>) Class.forName(filters.get(selection)).newInstance();
			addFilter(position, selectionInstance);
		} catch (IllegalAccessException iae) {
			logger.error("Error instantiating filter " + Class.forName(selection).getName() + " cause: " + iae, iae);
		} catch (InstantiationException ie) {
			logger.error("Error instantiating filter " + Class.forName(selection).getName() + " cause: " + ie, ie);
		}
		selection = null;
		Roma.fieldChanged(this, "selection");
		Roma.fieldChanged(this, "filters");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public void addSubOperation(Integer position) {
		if (position < 0)
			position = 0;
		QuerySubOperationDelimiter startDelimiter = new QuerySubOperationDelimiter(this);
		QuerySubOperationDelimiter endDelimiter = new QuerySubOperationDelimiter(this);
		startDelimiter.setDelimiter(QuerySubOperationDelimiter.BEGIN_DELIMITER);
		endDelimiter.setDelimiter(QuerySubOperationDelimiter.END_DELIMITER);
		startDelimiter.setConnectedDelimiter(endDelimiter);
		endDelimiter.setConnectedDelimiter(startDelimiter);
		operation.add(position, startDelimiter);
		operation.add(position + 1, endDelimiter);
		updateOperationAfterAdd(position, operation.size(), true);
		Roma.fieldChanged(this, "operation");
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addFilter(Integer position, BaseFilter<?> iSelectedFilter) {
		try {
			Constructor<? extends QueryCondition> conditionConstructor = iSelectedFilter.getQueryConditionClass().getConstructor(BaseFilter.class, QueryOperation.class);
			QueryCondition condition = conditionConstructor.newInstance(iSelectedFilter, this);
			if (position < 0)
				position = 0;
			operation.add(position, condition);
			int operationSize = operation.size();
			updateOperationAfterAdd(position, operationSize, false);
			Roma.fieldChanged(this, "operation");
		} catch (SecurityException se) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + se, se);
		} catch (NoSuchMethodException nsme) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + nsme, nsme);
		} catch (IllegalArgumentException iare) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + iare, iare);
		} catch (InstantiationException ie) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + ie, ie);
		} catch (IllegalAccessException iae) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + iae, iae);
		} catch (InvocationTargetException ite) {
			logger.error("Error instantiating filter " + iSelectedFilter.getClass().getName() + " cause: " + ite, ite);
		}
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void removeFilter(Integer position) {
		if (position < 0)
			return;
		operation.remove(position.intValue());
		updateOperationAfterRemove(position);

		Roma.fieldChanged(this, "operation");
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void removeSubOperation(Integer startPosition, Integer endPosition) {
		for (int i = 1; i <= ((endPosition - startPosition) + 1); i++)
			operation.remove(startPosition.intValue());
		updateOperationAfterRemove(startPosition);
		Roma.fieldChanged(this, "operation");
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void showFilters() {
		Roma.setFeature(this, "filters", ViewFieldFeatures.VISIBLE, true);
		Roma.setFeature(this, "add", ViewActionFeatures.VISIBLE, true);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void hideFilters() {
		Roma.setFeature(this, "filters", ViewFieldFeatures.VISIBLE, false);
		Roma.setFeature(this, "add", ViewActionFeatures.VISIBLE, false);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void showEditFilter() {
		Roma.flow().popup(editCondition);
		Roma.fieldChanged(this, "operation");
	}

	protected void loadFilters() {
		I18NAspect i18nAspect = Roma.i18n();
		filters.put(i18nAspect.get("QuerySubOperation.filter.label"), "SubOperation");
		List<Class<?>> filterSchemas = Roma.component(SchemaClassResolver.class).getLanguageClassByInheritance(BaseFilter.class);
		for (Class<?> schema : filterSchemas) {
			if (schema.getName().equals(AbstractBaseFilter.class.getName()) || schema.getName().equals(BaseFilter.class.getName())) {
				continue;
			}
			String filterName = i18nAspect.get(schema.getSimpleName() + ".label");
			if (filterName == null) {
				filterName = schema.getSimpleName();
			}

			filters.put(filterName.trim(), schema.getName());
		}

		Set<Entry<String, String>> filtersSet = filters.entrySet();
		List<Entry<String, String>> filterToOrder = new ArrayList<Entry<String, String>>();
		filterToOrder.addAll(filtersSet);

		Collections.sort(filterToOrder, new Comparator<Entry<String, String>>() {

			public int compare(Entry<String, String> arg0, Entry<String, String> arg1) {
				return arg0.getKey().compareTo(arg1.getKey());

			}
		});

		filters.clear();
		for (Entry<String, String> entry : filterToOrder) {
			filters.put(entry.getKey(), entry.getValue());
		}
	}

	private Set<String> updateResult(Set<String> result, String operationMode, Set<String> queryResult) {
		if (result == null) {
			result = queryResult;
		} else if (operationMode.equals(QueryOperator.OPERATOR_AND)) {
			result.retainAll(queryResult);
		} else {
			result.addAll(queryResult);
		}
		return result;
	}

	protected void updateOperationAfterRemove(Integer position) {
		int operationSize = operation.size();
		if (operationSize == 2 && isQuerySubOperation(operation.get(0)) && isQuerySubOperation(operation.get(1)))
			return;
		if ((position == operationSize - 1 && isQuerySubOperation(operation.get(position))) || (operationSize != 0 && position >= operationSize))
			operation.remove(position.intValue() - 1);
		else if (operationSize > 1) {
			operation.remove(position.intValue());
		}
		if (operationSize == 0) {
			positionToAdd = 0;
			showFilters();
		}
	}

	protected void updateOperationAfterAdd(Integer position, int operationSize, boolean subOperation) {
		if (operationSize > 1) {
			if (subOperation) {
				if (position != 0) {
					if (operationSize == position + 2) {
						operation.add(position, new QueryOperator(this));
					} else if (isQueryCondition(operation.get(position + 2))) {
						operation.add(position + 1, new QueryOperator(this));
					} else
						operation.add(position, new QueryOperator(this));
				} else if (operationSize > 2) {
					operation.add(position + 2, new QueryOperator(this));
				}
			} else if (operationSize == position + 1) {
				operation.add(position, new QueryOperator(this));
			} else if (isQueryCondition(operation.get(position + 1)) || (position == 0 && !subOperation)) {
				operation.add(position + 1, new QueryOperator(this));
			} else if (!subOperation && !isQuerySubOperation(operation.get(position - 1))) {
				operation.add(position, new QueryOperator(this));
			}
		}
	}

	protected boolean isQueryCondition(QueryItem item) {
		return item instanceof QueryCondition;
	}

	protected boolean isQuerySubOperation(QueryItem item) {
		return item instanceof QuerySubOperationDelimiter;
	}

	private void showSelectOnlyOneError() {
		MessageText msg = new MessageOk("delete", "Information");
		msg.setMessage("$CRUDMain.selectOnlyOne.error");
		msg.setIcon("information.gif");
		Roma.aspect(FlowAspect.class).popup(msg);
	}

}
