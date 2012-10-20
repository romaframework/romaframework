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

package org.romaframework.module.users.view.domain.principal;

import java.security.Principal;

import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.module.users.view.domain.baseaccount.BaseAccountSelect;
import org.romaframework.module.users.view.domain.basegroup.BaseGroupSelect;
import org.romaframework.module.users.view.domain.baseprofile.BaseProfileSelect;

public class SecurityPrincipalSelectorWindow extends PrincipalSelectorWindow {

	public SecurityPrincipalSelectorWindow(MessageResponseListener iListener, Object iContext) {
		super(iListener, iContext);
	}

	public SecurityPrincipalSelectorWindow(Principal iPrincipal, MessageResponseListener iListener, Object iContext) {
		super(iPrincipal, iListener, iContext);
	}

	public void any() {
		if (tabs.getActivePage() instanceof BaseAccountSelect)
			listener.responseMessage(this, "account:.*");
		if (tabs.getActivePage() instanceof BaseGroupSelect)
			listener.responseMessage(this, "group:.*");
		if (tabs.getActivePage() instanceof BaseProfileSelect)
			listener.responseMessage(this, "profile:.*");

		close();
	}
}