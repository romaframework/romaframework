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

package org.romaframework.module.users.view.domain.activitylog;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.frontend.domain.crud.CRUDMain;
import org.romaframework.module.users.domain.ActivityLog;

public class ActivityLogMain extends CRUDMain<ActivityLogListable> {

	@CoreField(embedded = AnnotationConstants.TRUE)
	protected ActivityLogFilter					filter;

	protected List<ActivityLogListable>	result;

	public ActivityLogMain() {
		this(null);
	}

	public ActivityLogMain(Object iBackObject) {
		super(ActivityLogListable.class, ActivityLogInstance.class, ActivityLogInstance.class, ActivityLogInstance.class);
		filter = new ActivityLogFilter();
		result = new ArrayList<ActivityLogListable>();
	}

	@Override
	public void showAll() {
		QueryByFilter query = new QueryByFilter(ActivityLog.class);
		query.addOrder("when", QueryByFilter.ORDER_DESC);

		searchByFilter(query);
	}

	@Override
	public void search() {
		QueryByFilter dynaFilter = new QueryByFilter(ActivityLog.class, QueryByFilter.PREDICATE_AND);
		dynaFilter.addItem("level", QueryByFilter.FIELD_MAJOR_EQUALS, filter.getEntity().getLevel());

		if (filter.getEntity().getCategory() != null)
			dynaFilter.addItem("category", QueryByFilter.FIELD_EQUALS, filter.getEntity().getCategory());

		if (filter.getEntity().getAccount() != null)
			dynaFilter.addItem("account", QueryByFilter.FIELD_EQUALS, filter.getEntity().getAccount());

		if (filter.getEntity().getNotes() != null && filter.getEntity().getNotes().length() > 0)
			dynaFilter.addItem("notes", QueryByFilter.FIELD_LIKE, filter.getEntity().getNotes());

		// SET DATE RANGE
		if (filter.getRangeFrom() != null)
			dynaFilter.addItem("when", QueryByFilter.FIELD_MAJOR_EQUALS, filter.getRangeFrom());
		if (filter.getRangeTo() != null)
			dynaFilter.addItem("when", QueryByFilter.FIELD_MINOR_EQUALS, filter.getRangeTo());

		dynaFilter.addOrder("when", QueryByFilter.ORDER_DESC);

		searchByFilter(dynaFilter);
	}

	@Override
	public Object create() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		return null;
	}

	@Override
	public Object read() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return null;
	}

	@Override
	public Object update() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return null;
	}

	@Override
	public ActivityLogFilter getFilter() {
		return filter;
	}

	@Override
	public List<ActivityLogListable> getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setResult(Object iValue) {
		result = (List<ActivityLogListable>) iValue;
	}
}
