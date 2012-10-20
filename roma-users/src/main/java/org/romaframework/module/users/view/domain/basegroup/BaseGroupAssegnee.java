/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseGroup;
import org.romaframework.module.users.view.domain.baseaccount.BaseAccountMain;

public class BaseGroupAssegnee extends BaseGroupSelect {
	protected BaseAccountMain	source;

	public BaseGroupAssegnee(BaseAccountMain iSource) {
		this.source = iSource;
	}

	@Override
	public void select() {
		BaseGroup selectedGroup = ((BaseGroupListable) getOnlyOneSelectedItem(getSelection())).getEntity();

		BaseAccount detailedAccount;
		for (Object account : source.getSelection()) {
			detailedAccount = (BaseAccount) loadObjectDetails(account);
			if (detailedAccount.addGroup(selectedGroup))
				source.getRepository().update(detailedAccount);
		}

		cancel();
	}
}
