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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.romaframework.aspect.authentication.AuthenticationAspect;
import org.romaframework.aspect.authentication.AuthenticationAspectAbstract;
import org.romaframework.aspect.authentication.LoginListener;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.session.SessionAccount;
import org.romaframework.aspect.validation.annotation.ValidationAction;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.aspect.view.screen.config.ScreenManager;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.module.users.UsersAuthentication;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.listener.DefaultLoginListener;

/**
 * Base class for login.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@CoreClass(orderFields = "userName userPassword languages")
public class Login implements MessageResponseListener {

	public static final String			PAR_FIRST_FORM_TO_DISPLAY	= "_Login.firstFormToDisplay";

	@ValidationField(required = AnnotationConstants.TRUE)
	private String									userName;

	@ValidationField(required = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_PASSWORD)
	private String									userPassword;

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "language")
	protected Set<String>						languages									= new HashSet<String>();

	protected AuthenticationAspect	authManager;

	private LoginListener						listener;

	protected LoginListener getListener() {
		if (listener == null) {
			listener = new DefaultLoginListener();
		}
		return listener;
	}

	public Login(String iScreenName) {
		this();

		// ROMA TUTORIAL: SET INITIAL SCREEN
		Screen screen = Roma.component(ScreenManager.class).getScreen(iScreenName);
		Roma.view().setScreen(screen);
	}

	public Login(String iUser, String iPassword) {
		this();
		userName = iUser;
		userPassword = iPassword;

		Roma.fieldChanged(this, "userName userPassword");
	}

	public Login() {
		authManager = Roma.component(AuthenticationAspect.class);
		Set<Locale> availableLocales = Roma.i18n().getAvailableLanguages();
		for (Locale l : availableLocales)
			languages.add(l.getDisplayLanguage());
	}

	/**
	 * This method is invoked upon authentication succeed. By default it shows the HomePage configured in the UserProfile. Override
	 * this if you want to change the standard behavior.
	 */
	protected void onSuccess() {
		getListener().onSuccess();
	}

	/**
	 * On error throws a UserException. Override this if you want to change the standard behavior.
	 */
	protected void onError() {
		onError(null);
	}

	/**
	 * On error throws a UserException. Override this if you want to change the standard behavior.
	 */
	protected void onError(Throwable t) {
		// PROPAGATES THE ERROR TO THE LISTENER TO BEING HANDLED
		getListener().onError(t);
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public String getAlgorithm() {
		return AuthenticationAspectAbstract.DEF_ALGORITHM;
	}


	@Persistence(mode = PersistenceConstants.MODE_TX)
	@ValidationAction(validate = AnnotationConstants.TRUE)
	@ViewAction(submit = AnnotationConstants.TRUE, render = ViewConstants.RENDER_BUTTON)
	public void login() {
		if (userName == null || userName.length() == 0)
			return;

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put(UsersAuthentication.PAR_ALGORITHM, getAlgorithm());
			BaseAccount account = (BaseAccount) authManager.authenticate(userName, userPassword, params);
			if (AccountManagementUtility.isPasswordExpired(account)) {
				showChangePassword();
			} else {
				if (account != null && account.isChangePasswordNextLogin() != null && account.isChangePasswordNextLogin().booleanValue())
					showChangePassword();
				else
					onSuccess();
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	private void showChangePassword() {
		ChangePassword changePw = new ChangePassword((BaseAccount) authManager.getCurrentAccount(), this);
		changePw.setOldPassword(userPassword);
		Roma.flow().popup(changePw);
	}

	public void responseMessage(Message iMessage, Object iResponse) {
		onSuccess();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	protected void setCurrentAccount(SessionAccount iAccount) {
		Roma.session().getActiveSessionInfo().setAccount(iAccount);
	}

	public Set<String> getLanguages() {
		return languages;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public String getLanguage() {
		Locale l = Roma.session().getActiveLocale();
		if (l == null)
			return null;
		return l.getDisplayLanguage();
	}

	public void setLanguage(String iLanguage) {
		if (iLanguage == null)
			return;

		for (Locale locale : Locale.getAvailableLocales()) {
			if (locale.getDisplayLanguage().equals(iLanguage)) {
				Roma.session().setActiveLocale(locale);
				Roma.fieldChanged(this);
				break;
			}
		}
	}

	public void setListener(LoginListener iListener) {
		listener = iListener;
	}
}
