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

package org.romaframework.module.users.view.domain.basegroup;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.frontend.domain.crud.CRUDMain;
import org.romaframework.module.users.domain.BaseGroup;

public class BaseGroupMain extends CRUDMain<BaseGroupListable> {

	@CoreField(embedded = AnnotationConstants.TRUE)
	protected BaseGroupFilter					filter;

	protected List<BaseGroupListable>	result;

	public BaseGroupMain() {
		this(null);
	}

	public BaseGroupMain(Object iBackObject) {
		super(BaseGroupListable.class, BaseGroupInstance.class, BaseGroupInstance.class, BaseGroupInstance.class);
		filter = new BaseGroupFilter();
		result = new ArrayList<BaseGroupListable>();
	}

	@Override
	public BaseGroupFilter getFilter() {
		return filter;
	}

	@Override
	public List<BaseGroupListable> getResult() {
		return result;
	}

	@Override
	public void showAll() {
		QueryByFilter query = new QueryByFilter(BaseGroup.class);
		searchByFilter(query);
	}

	@Override
	public void search() {
		QueryByFilter query = new QueryByFilter(BaseGroup.class);
		BaseGroupFilter filter = getFilter();

		if (filter.getEntity().getName() != null)
			query.addItem("name", QueryByFilter.FIELD_EQUALS, filter.getEntity().getName());

		if (filter.getEntity().getNotes() != null)
			query.addItem("notes", QueryByFilter.FIELD_EQUALS, filter.getEntity().getNotes());

		super.searchByExample(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setResult(Object iValue) {
		result = (List<BaseGroupListable>) iValue;
	}
}
