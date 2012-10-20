/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.module.users.logger;

import org.romaframework.aspect.logging.AbstractLogger;
import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.ActivityLog;
import org.romaframework.module.users.domain.ActivityLogCategory;

/**
 * Is logs the event using the ActivityLog of the users module
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class DBLogger extends AbstractLogger {

	public DBLogger(LoggingAspect loggingAspect) {
		super(loggingAspect);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#getModes()
	 */
	public String[] getModes() {

		String[] modes = { LoggingConstants.MODE_DB };
		return modes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#print(int, java.lang.String, java.lang.String)
	 */
	public void print(int level, String category, String message) {
		PersistenceAspect db = Roma.context().persistence();
		QueryByFilter filter = new QueryByFilter(ActivityLogCategory.class);
		filter.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		filter.addItem("name", QueryByFilter.FIELD_EQUALS, category);

		ActivityLogCategory activityLogCategory = db.queryOne(filter);
		if (activityLogCategory == null) {
			activityLogCategory = db.createObject(new ActivityLogCategory(category), PersistenceAspect.STRATEGY_DETACHING);
		}
		ActivityLog log = new ActivityLog(level, activityLogCategory, message);

		db.createObject(log);

	}

}
