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

package org.romaframework.module.users.view.domain.menu;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.annotation.FlowAction;
import org.romaframework.module.users.view.domain.activitylog.ActivityLogMain;
import org.romaframework.module.users.view.domain.baseaccount.BaseAccountMain;
import org.romaframework.module.users.view.domain.basegroup.BaseGroupMain;
import org.romaframework.module.users.view.domain.baseprofile.BaseProfileMain;
import org.romaframework.module.users.view.domain.configuration.ConfigurationBaseAccount;

@CoreClass(orderActions = "accounts profiles groups activityLogs")
public class UsersPanel {

	@FlowAction(next = BaseAccountMain.class, position = "body")
	public void accounts() {
	}

	@FlowAction(next = BaseProfileMain.class, position = "body")
	public void profiles() {
	}

	@FlowAction(next = BaseGroupMain.class, position = "body")
	public void groups() {
	}

	@FlowAction(next = ConfigurationBaseAccount.class, position = "body")
	public void configuration() {
	}
	
	@FlowAction(next = ActivityLogMain.class, position = "body")
	public void activityLogs() {
	}

}
