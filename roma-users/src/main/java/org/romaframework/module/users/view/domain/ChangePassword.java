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
package org.romaframework.module.users.view.domain;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.romaframework.aspect.authentication.AuthenticationAspect;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.message.MessageOk;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.page.Page;
import org.romaframework.module.users.UsersModule;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.repository.BaseAccountRepository;

/**
 * Change password simple form.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@CoreClass(orderFields = "message oldPassword password confirmPassword", orderActions = "change cancel")
public class ChangePassword extends Page {

	@ViewField(render = "password")
	private String									oldPassword;

	@ViewField(render = "password")
	private String									password;

	@ViewField(render = "password")
	private String									confirmPassword;

	private MessageResponseListener	listener;

	private BaseAccount							account;

	public ChangePassword(BaseAccount iAccount, MessageResponseListener iListener) {
		account = iAccount;
		listener = iListener;
	}

	@ViewAction(render=ViewConstants.RENDER_BUTTON)
	public void cancel() {
		back();
	}

	/**
	 * Change the password, reset the flag
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@ViewAction(render=ViewConstants.RENDER_BUTTON)
	public void change() throws NoSuchAlgorithmException {
		if (password == null && confirmPassword == null)
			return;

		if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
			Roma.aspect(FlowAspect.class).popup(new MessageOk("error", "", null, "$ChangePassword.change.error"));
			return;
		}

		String cypherOldPassword = null;
		if (oldPassword != null)
			cypherOldPassword = Roma.aspect(AuthenticationAspect.class).encryptPassword(oldPassword);
		if (cypherOldPassword == null || !cypherOldPassword.equals(account.getPassword())) {
			Roma.flow().popup(new MessageOk("error", "", null, "$ChangePassword.oldPassword.error"));
			return;
		}
		if (!AccountManagementUtility.isPasswordMathedRegExpression(password)) {
			Roma.flow().popup(new MessageOk("error", "", null, "$ChangePassword.invalidPassword.error"));
			return;
		}
		if (!AccountManagementUtility.isPasswordUnused(account, password)) {
			Roma.flow().popup(new MessageOk("error", "", null, "$ChangePassword.alreadyUsed.error"));
			return;
		}

		// UPDATE THE OBJECT PERSISTENTLY
		account.setPassword(password);
		account.setChangePasswordNextLogin(false);
		account.setLastModified(new Date());
		account.setLastPasswordUpdate(new Date());
		account = Roma.component(BaseAccountRepository.class).update(account, PersistenceAspect.STRATEGY_DETACHING);
		Roma.session().getActiveSessionInfo().setAccount(account);

		String mess = "$ChangePassword.message.feature";
		Integer passwordPeriod = Roma.component(UsersModule.class).getPasswordPeriod();
		Integer accountPeriod = Roma.component(UsersModule.class).getAccountPeriod();
		if (passwordPeriod != null) {
			mess += Roma.i18n().get(ChangePassword.class, "message.password",I18NType.LABEL, passwordPeriod);
		}
		if (accountPeriod != null) {
			Integer scadenza = (Math.round((account.getLastModified().getTime()) / AccountManagementUtility.DAY_MILLISECONDS) + accountPeriod)
					- Math.round((new Date()).getTime() / AccountManagementUtility.DAY_MILLISECONDS);
			mess += Roma.i18n().get(this, "message.account", I18NType.LABEL,scadenza);
		}
		Roma.flow().popup(new MessageOk("CHANGE PASSWORD", "", null, mess));
		back();
		// WAKE UP LISTENER
		if (listener != null)
			listener.responseMessage(null, Boolean.TRUE);
	}

	@ViewField(render = ViewConstants.RENDER_LABEL)
	public String getMessage() {
		return Roma.i18n().get("ChangePassword.message.text");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String userPassword) {
		password = userPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
