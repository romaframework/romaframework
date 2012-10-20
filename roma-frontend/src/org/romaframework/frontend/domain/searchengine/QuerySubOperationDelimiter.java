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

import java.util.Calendar;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;

/**
 * @author molino
 * 
 */
public class QuerySubOperationDelimiter implements QueryItem {

	public static final String						BEGIN_DELIMITER	= "(";

	public static final String						END_DELIMITER		= ")";

	protected QueryOperation							operation;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String											delimiter				= BEGIN_DELIMITER;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected QuerySubOperationDelimiter	connectedDelimiter;

	protected Long												timestamp				= Calendar.getInstance().getTimeInMillis();

	public QuerySubOperationDelimiter(QueryOperation iOperation) {
		operation = iOperation;
	}

	public QueryOperation getOperation() {
		return operation;
	}

	public String getText() {
		return delimiter;
	}

	public QuerySubOperationDelimiter getConnectedDelimiter() {
		return connectedDelimiter;
	}

	public void setConnectedDelimiter(QuerySubOperationDelimiter connectedDelimiter) {
		this.connectedDelimiter = connectedDelimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void onText() {
		showActions();
	}

	@ViewField(position = "form://itemActions", style = "searchEngineEditFilter")
	public void edit() {
		operation.showFilters();
		if (delimiter.equals(BEGIN_DELIMITER))
			operation.setPositionToAdd(getPosition() + 1);
		else
			operation.setPositionToAdd(getPosition());
		hideActions();
	}

	@ViewField(position = "form://itemActions", style = "searchEngineRemoveFilter")
	public void remove() {
		Integer startPosition;
		Integer endPosition;
		if (delimiter.equals(BEGIN_DELIMITER)) {
			startPosition = getPosition();
			endPosition = connectedDelimiter.getPosition();
		} else {
			startPosition = connectedDelimiter.getPosition();
			endPosition = getPosition();
		}
		operation.removeSubOperation(startPosition, endPosition);
		hideActions();
	}

	public void onDispose() {
	}

	public void onShow() {
		Roma.setFeature(this, "addRight", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "addLeft", ViewActionFeatures.VISIBLE, false);
		hideActions();
	}

	@ViewField(position = "form://itemActions", style = "searchEngineAddLeftFilter")
	public void addLeft() {
		operation.showFilters();
		operation.setPositionToAdd(0);
		hideActions();
	}

	@ViewField(position = "form://itemActions", style = "searchEngineAddRightFilter")
	public void addRight() {
		operation.showFilters();
		operation.setPositionToAdd(operation.getOperation().size());
		hideActions();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass().equals(obj.getClass())) {
			return delimiter.equals(((QuerySubOperationDelimiter) obj).delimiter) && timestamp.equals(((QuerySubOperationDelimiter) obj).timestamp);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return delimiter.hashCode() + timestamp.hashCode();
	}

	private void hideActions() {
		Roma.setFeature(this, "remove", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "edit", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "addRight", ViewActionFeatures.VISIBLE, false);
		Roma.setFeature(this, "addLeft", ViewActionFeatures.VISIBLE, false);
	}

	private void showActions() {
		Roma.setFeature(this, "remove", ViewActionFeatures.VISIBLE, true);
		Roma.setFeature(this, "edit", ViewActionFeatures.VISIBLE, true);
		if (delimiter.equals(END_DELIMITER) && (getPosition() + 1 == operation.getOperation().size()))
			Roma.setFeature(this, "addRight", ViewActionFeatures.VISIBLE, true);
		if (delimiter.equals(BEGIN_DELIMITER) && getPosition() == 0)
			Roma.setFeature(this, "addLeft", ViewActionFeatures.VISIBLE, true);
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Integer getPosition() {
		return operation.getOperation().indexOf(this);
	}

	@Override
	public String toString() {
		return getText();
	}
}
