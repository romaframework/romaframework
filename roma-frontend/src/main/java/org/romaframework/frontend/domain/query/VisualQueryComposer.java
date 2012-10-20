/*
 * Copyright 2007 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.frontend.domain.query;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.command.CommandContext;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.schema.SchemaClass;

@CoreClass(orderFields = "classesList filters", orderActions = "ok cancel")
public class VisualQueryComposer implements Refreshable {

	// ///////////CONSTANTS/////////////////////

	public final static String					CONTEXT_MARKER	= "#";

	// ///////////FIELDS/////////////////////

	private List<ElementEventListener>	listeners				= new ArrayList<ElementEventListener>();

	@ViewField(visible = AnnotationConstants.FALSE)
	private Class<?>										targetClass;

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "targetClass", label = "", position = "form://classCombo")
	private List<SchemaClass>						classesList			= Roma.schema().getSchemaClassesByPackage(Roma.component(ApplicationConfiguration.class).getApplicationPackage() + ".domain");

	@ViewField(visible = AnnotationConstants.FALSE)
	private VisualQueryFilter[]					filterSelection;
	@ViewField(render = ViewConstants.RENDER_TABLEEDIT, label = "", selectionField = "filterSelection", position = "form://filters")
	private List<VisualQueryFilter>			filters					= new ArrayList<VisualQueryFilter>();

	private CommandContext							context;

	// ///////////CONSTRUCTORS/////////////////////

	public VisualQueryComposer() {
		this(null);
	}

	public VisualQueryComposer(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	// ///////////METHODS/////////////////////

	public List<VisualQueryFilter> getFilters() {
		return filters;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public QueryByFilter query() {
		if (this.targetClass == null)
			return null;
		QueryByFilter query = new QueryByFilter(targetClass);
		for (VisualQueryFilter filter : filters) {
			if (filter.getFieldName() == null)
				continue;
			if (filter.getOperator() == null)
				continue;

			Object fieldValue = filter.getFieldValue();
			if ((fieldValue instanceof String) && fieldValue.toString().startsWith(CONTEXT_MARKER))
				fieldValue = context.getParameter(fieldValue.toString().substring(CONTEXT_MARKER.length()));
			query.addItem(filter.getFieldName(), filter.getOperator(), fieldValue);
		}
		return query;
	}

	public void onFiltersAdd() {
		filters.add(new VisualQueryFilter(context, this, targetClass));
		Roma.fieldChanged(this, "filters");
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		if (this.targetClass == null || !this.targetClass.equals(targetClass)) {
			filters.clear();
			refresh();
		}
		this.targetClass = targetClass;
	}

	public List<SchemaClass> getClassesList() {
		return classesList;
	}

	public void setClassesList(List<SchemaClass> classesList) {
		this.classesList = classesList;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		Roma.fieldChanged(this, "filters");
	}

	@ViewAction(position = "form://buttonOk")
	public void ok() {
		notifyOk();
		Roma.aspect(FlowAspect.class).back();
	}

	private void notifyOk() {
		for (ElementEventListener listener : listeners)
			listener.eventOk(this);
	}

	@ViewAction(position = "form://buttonCancel")
	public void cancel() {
		notifyCancel();
		Roma.aspect(FlowAspect.class).back();
	}

	private void notifyCancel() {
		for (ElementEventListener listener : listeners)
			listener.eventCancel(this);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addEventListener(ElementEventListener listener) {
		if (listener != null)
			listeners.add(listener);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void removeEventListener(ElementEventListener listener) {
		if (listener != null)
			listeners.remove(listener);
	}

	public VisualQueryFilter[] getFilterSelection() {
		return filterSelection;
	}

	public void setFilterSelection(VisualQueryFilter[] filterSelection) {
		this.filterSelection = filterSelection;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public CommandContext getContext() {
		return context;
	}

	public void setContext(CommandContext context) {
		this.context = context;
	}
}
