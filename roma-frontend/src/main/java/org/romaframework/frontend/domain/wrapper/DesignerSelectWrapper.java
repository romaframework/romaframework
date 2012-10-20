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
package org.romaframework.frontend.domain.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.SelectionMode;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;

public class DesignerSelectWrapper<T> extends Observable implements ObjectWrapper {
	@ViewField(label = "", render = "select", selectionField = "selected", selectionMode = SelectionMode.SELECTION_MODE_VALUE)
	protected T[]	values;
	@ViewField(visible = AnnotationConstants.FALSE)
	protected T		selected;

	public DesignerSelectWrapper(Set<T> selectValues) {
		values = selectValues.toArray((T[]) new Object[selectValues.size()]);
	}

	public DesignerSelectWrapper(Set<T> selectValues, T iInitialValue) {
		values = selectValues.toArray((T[]) new Object[selectValues.size()]);
		selected = iInitialValue;
	}

	public T getSelected() {
		return selected;
	}

	public void setSelected(T command) {

		this.selected = command;
		setChanged();
		notifyObservers();
	}

	public T[] getValues() {
		return values;
	}

	public void setValues(T[] commands) {
		this.values = commands;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getFinalValue() {
		return selected;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addElements(Set<T> iToAdd) {
		List<T> aux = Arrays.asList(values);
		aux.addAll(iToAdd);
		values = aux.toArray((T[]) new Object[aux.size()]);
	}

	@Override
	@ViewAction(visible = AnnotationConstants.FALSE)
	public synchronized void deleteObservers() {

		super.deleteObservers();
	}

	@Override
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void notifyObservers() {

		super.notifyObservers();
	}

}
