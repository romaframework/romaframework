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

package org.romaframework.module.users;

import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.ActivityLog;
import org.romaframework.module.users.domain.BaseAccount;

/**
 * Log user and system activities.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ActivityLogHelper {

	private static ActivityLogHelper	instance	= new ActivityLogHelper();

	protected ActivityLogHelper() {
	}

	/**
	 * Use LoggingAspect instead.
	 */
	@Deprecated
	public void log(int iLevel, String iCategoryName, String iNotes) {
		Roma.aspect(LoggingAspect.class).log(iLevel, iCategoryName, LoggingConstants.MODE_DB, iNotes);
	}

	public static ActivityLogHelper getInstance() {
		return instance;
	}

	public static ActivityLog findLastActivityOfAccount(BaseAccount iAccount) {
		QueryByFilter query = new QueryByFilter(ActivityLog.class);
		query.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		query.setMode(PersistenceAspect.FULL_MODE_LOADING);

		query.addItem("account", QueryByFilter.FIELD_EQUALS, iAccount);
		query.addOrder("when", QueryByFilter.ORDER_DESC);
		return Roma.context().persistence().queryOne(query);
	}
}
