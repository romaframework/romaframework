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

package org.romaframework.module.users.view.domain.baseprofile;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.frontend.domain.crud.CRUDMain;
import org.romaframework.module.users.domain.BaseProfile;

public class BaseProfileMain extends CRUDMain<BaseProfileListable> {

	@CoreField(embedded = AnnotationConstants.TRUE)
	protected BaseProfileFilter					filter;

	protected List<BaseProfileListable>	result;

	public BaseProfileMain() {
		super(BaseProfileListable.class, BaseProfileInstance.class, BaseProfileInstance.class, BaseProfileInstance.class);

		filter = new BaseProfileFilter();
		result = new ArrayList<BaseProfileListable>();
	}

	@Override
	public void showAll() {
		QueryByFilter query = new QueryByFilter(BaseProfile.class);
		searchByFilter(query);
	}

	@Override
	public void search() {
		QueryByFilter addFilter = new QueryByFilter(BaseProfile.class);

		addFilter.addOrder("name");

		searchByExample(addFilter);
	}

	@Override
	public BaseProfileFilter getFilter() {
		return filter;
	}

	@Override
	public List<BaseProfileListable> getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setResult(Object iValue) {
		result = (List<BaseProfileListable>) iValue;
	}
}
