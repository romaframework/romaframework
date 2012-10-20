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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;

/**
 * @author molino
 * 
 */
public class QueryOperator implements QueryItem {

	public static final String	OPERATOR_AND	= "and";
	public static final String	OPERATOR_OR		= "or";

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String						operator			= OPERATOR_AND;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected QueryOperation		operation;

	public QueryOperator(QueryOperation iOperation) {
		operation = iOperation;
	}

	public String getText() {
		return operator;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public QueryOperation getOperation() {
		return operation;
	}

	public void setOperation(QueryOperation operation) {
		this.operation = operation;
	}

	public void onText() {
		if (operator.equals(OPERATOR_AND))
			operator = OPERATOR_OR;
		else
			operator = OPERATOR_AND;
		Roma.fieldChanged(this, "text");
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void remove() {
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addLeft() {
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addRight() {
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void edit() {
	}

	public void onDispose() {
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null && getClass().equals(arg0.getClass())) {
			return operator.equals(((QueryOperator) arg0).operator);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return operator.hashCode();
	}

	public void onShow() {
		Roma.setFeature(this, "addRight", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "addLeft", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "remove", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "edit", ViewActionFeatures.VISIBLE, false);
	}

	@Override
	public String toString() {
		return getText();
	}
}
