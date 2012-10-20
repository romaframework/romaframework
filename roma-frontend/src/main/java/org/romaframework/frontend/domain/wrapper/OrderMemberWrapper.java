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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;

@CoreClass(orderActions = "up down")
public class OrderMemberWrapper<T> implements ObjectWrapper, Refreshable {
	@ViewField(visible = AnnotationConstants.FALSE)
	private OrderMemberWrapperElement					selected;
	@ViewField(render = ViewConstants.RENDER_TABLE, selectionField = "selected", enabled = AnnotationConstants.FALSE, position = "form://fields")
	protected List<OrderMemberWrapperElement>	toOrder	= new ArrayList<OrderMemberWrapperElement>();

	public OrderMemberWrapper(Collection<T> iToOrder) {
		if (iToOrder != null) {
			for (T element : iToOrder) {
				this.toOrder.add(new OrderMemberWrapperElement(element));
			}
		}
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getFinalValue() throws Exception {
		List<T> result = new LinkedList<T>();
		for (OrderMemberWrapperElement element : toOrder) {
			result.add((T) element.getInitialObject());
		}
		return result;
	}

	@ViewField(position = "form://actions")
	public void up() {
		if (selected != null) {
			int index = toOrder.indexOf(selected);
			if (index != 0) {
				toOrder.add(index - 1, selected);
				toOrder.remove(index + 1);
				this.refresh();
			}
		}
	}

	@ViewField(position = "form://actions")
	public void down() {
		if (selected != null) {
			int index = toOrder.indexOf(selected);
			if (index != toOrder.size() - 1) {
				OrderMemberWrapperElement toMove = toOrder.get(index + 1);
				toOrder.set(index + 1, selected);
				toOrder.set(index, toMove);
				this.refresh();
			}
		}
	}

	public Object getSelected() {
		return selected;
	}

	public void setSelected(OrderMemberWrapperElement selected) {
		this.selected = selected;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		Roma.fieldChanged(this, "toOrder");
	}

	public List<OrderMemberWrapperElement> getToOrder() {
		return toOrder;
	}
}
