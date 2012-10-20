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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.crud.CRUDSelect;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.page.ContainerPage;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseGroup;
import org.romaframework.module.users.domain.BaseProfile;
import org.romaframework.module.users.view.domain.baseaccount.BaseAccountSelect;
import org.romaframework.module.users.view.domain.basegroup.BaseGroupSelect;
import org.romaframework.module.users.view.domain.baseprofile.BaseProfileSelect;

public class PrincipalSelectorWindow extends Message {

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	protected ContainerPage	tabs	= new ContainerPage();

	protected Principal			selectedPrincipal;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Object				context;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Principal			selection;

	public PrincipalSelectorWindow(MessageResponseListener iListener, Object iContext) {
		this((Principal) null, iListener, iContext);
	}

	public PrincipalSelectorWindow(Principal iPrincipal, MessageResponseListener iListener, Object iContext) {
		super("principalSelector", "Select a principal", iListener);

		context = iContext;

		CRUDSelect<?> tab = new BaseAccountSelect();
		tab.setSource(this, "selection");
		tabs.addPage("Accounts", tab);

		tab = new BaseGroupSelect();
		tab.setSource(this, "selection");
		tabs.addPage("Groups", tab);

		tab = new BaseProfileSelect();
		tab.setSource(this, "selection");
		tabs.addPage("Profiles", tab);
	}

	@Override
	public void onShow() {
		super.onShow();

		if (selectedPrincipal != null)
			if (selectedPrincipal instanceof BaseAccount)
				tabs.setActivePage("Accounts");
			else if (selectedPrincipal instanceof BaseGroup)
				tabs.setActivePage("Groups");
			else if (selectedPrincipal instanceof BaseProfile)
				tabs.setActivePage("Profiles");
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}

	public ContainerPage getTabs() {
		return tabs;
	}

	public Principal getSelection() {
		return selection;
	}

	public void setSelection(Principal selection) {
		this.selection = selection;
		listener.responseMessage(this, selection);
	}
}