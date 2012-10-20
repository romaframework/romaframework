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

package org.romaframework.module.users.view.domain.baseaccount;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.romaframework.aspect.authentication.AuthenticationAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItemGroup;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.crud.CRUDFilter;
import org.romaframework.frontend.domain.wrapper.SelectWrapper;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseGroup;

@ViewClass(label = "")
public class BaseAccountFilter extends CRUDFilter<BaseAccount> {

	protected SelectWrapper<BaseGroup>	groups	= loadGroupList();

	protected BaseGroup									selectedGroup;

	public BaseAccountFilter() {
		super(new BaseAccount());
	}

	public BaseAccountFilter(BaseAccount iAccount) {
		super(iAccount);
	}

	private SelectWrapper<BaseGroup> loadGroupList() {

		PersistenceAspect db = Roma.context().persistence();
		Set<BaseGroup> userGroups = db.refreshObject((BaseAccount) Roma.component(AuthenticationAspect.class).getCurrentAccount(),
				PersistenceAspect.FULL_MODE_LOADING, PersistenceAspect.STRATEGY_DETACHING).getGroups();

		if (userGroups == null || userGroups.size() == 0) {
			userGroups = new HashSet<BaseGroup>();
			QueryByFilter allGroupsQuery = new QueryByFilter(BaseGroup.class);
			allGroupsQuery.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
			List<BaseGroup> allGroups = db.query(allGroupsQuery);
			if (allGroups != null && allGroups.size() > 0) {
				userGroups.addAll(allGroups);
			}
		}

		SelectWrapper<BaseGroup> result = new SelectWrapper<BaseGroup>(userGroups, this, "selectedGroup", true);
		return result;
	}

	public BaseGroup getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(BaseGroup selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public SelectWrapper<BaseGroup> getGroups() {
		return groups;
	}

	@Override
	protected QueryByFilter getAdditionalFilter() {
		QueryByFilter query = super.getAdditionalFilter();
		if (query == null) {
			query = new QueryByFilter(BaseAccount.class);
		}

		if (getSelectedGroup() != null) {
			query.addItem("groups", QueryByFilter.FIELD_CONTAINS, getSelectedGroup());
		} else {
			Set<BaseGroup> userGroups = Roma
					.context()
					.persistence()
					.refreshObject((BaseAccount) Roma.component(AuthenticationAspect.class).getCurrentAccount(), PersistenceAspect.FULL_MODE_LOADING,
							PersistenceAspect.STRATEGY_DETACHING).getGroups();
			if (userGroups != null && userGroups.size() > 0) {
				QueryByFilterItemGroup groupFilter = new QueryByFilterItemGroup(QueryByFilter.PREDICATE_OR);
				for (BaseGroup g : userGroups) {
					groupFilter.addItem("groups", QueryByFilter.FIELD_CONTAINS, g);
				}
				query.addItem(groupFilter);
			}
		}

		if (getEntity().getProfile() != null) {
			query.addItem("profile", QueryByFilter.FIELD_EQUALS, getEntity().getProfile());
		}

		if (getEntity().getName() != null) {
			query.addItem("name", QueryByFilter.FIELD_LIKE, getEntity().getName());
		}

		if (getEntity().getNotes() != null) {
			query.addItem("notes", QueryByFilter.FIELD_LIKE, getEntity().getNotes());
		}

		if (query.getItems() == null || query.getItems().size() == 0) {
			return null;
		}
		return query;
	}
}
