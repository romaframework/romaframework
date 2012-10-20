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

import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryOperator;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.command.CommandContext;
import org.romaframework.core.config.Refreshable;

@CoreClass(orderFields = "fieldName fieldComposer operators fieldValue fromContext")
public class VisualQueryFilter implements Refreshable {

	private final static String	FIELD_COMPOSER_LABEL	= "?";

	private Refreshable					parent;
	private String							fieldName;
	private Class<?>						queryClass;

	private CommandContext			context								= new CommandContext();

	@ViewField(visible = AnnotationConstants.FALSE)
	private QueryOperator				operator;
	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "operator")
	private QueryOperator[]			operators							= { QueryByFilter.FIELD_EQUALS, QueryByFilter.FIELD_MAJOR, QueryByFilter.FIELD_MAJOR_EQUALS, QueryByFilter.FIELD_MINOR,
			QueryByFilter.FIELD_MINOR_EQUALS, QueryByFilter.FIELD_NOT_EQUALS, QueryByFilter.FIELD_LIKE };

	private Object							fieldValue;

	public VisualQueryFilter(CommandContext context, Refreshable parent, Class<?> queryClass) {
		this.parent = parent;
		this.queryClass = queryClass;
		if (context != null)
			this.context = context;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@ViewField(label = "", render = ViewConstants.RENDER_BUTTON)
	public String getFieldComposer() {
		return FIELD_COMPOSER_LABEL;
	}

	public void onFieldComposer() {
		Roma.flow().popup(new AttributeChooser(queryClass, this, "fieldName"));
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public void setOperator(QueryOperator operator) {
		this.operator = operator;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public QueryOperator[] getOperators() {
		return operators;
	}

	public void setOperators(QueryOperator[] operators) {
		this.operators = operators;
	}

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "fromContextSelection")
	public Set<String> getFromContext() {
		return context.getParameters().keySet();
	}

	public void setFromContextSelection(Object[] selection) {
		if (selection != null && selection.length > 0)
			setFieldValue(VisualQueryComposer.CONTEXT_MARKER + selection[0]);
		parent.refresh();
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object[] getFromContextSelection() {
		return null;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		parent.refresh();
	}
}
