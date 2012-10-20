package org.romaframework.module.users.view.domain;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.portal.PortalPreferences;
import org.romaframework.module.users.domain.portal.PortalPreferencesHelper;
import org.romaframework.module.users.view.domain.portal.PortletPreferencesConfiguration;

@CoreClass(orderFields = "userName account", orderActions = "changePassword changePreferences configurePortlets logout")
public class UserInfo implements ViewCallback {
	protected String													userName;

	protected BaseAccount											account;

	protected Object													preferencesForm;

	protected ChangePassword									changePasswordForm;

	protected PortletPreferencesConfiguration	portletConfiguration;

	public UserInfo(Object iPreferencesForm) {
		preferencesForm = iPreferencesForm;
		account = (BaseAccount) Roma.session().getActiveSessionInfo().getAccount();
		if (account != null) {
			userName = account.toString();
			changePasswordForm = new ChangePassword(account, null);
			PortalPreferences preferences = PortalPreferencesHelper.getUserPreferences(account);
			if (preferences != null)
				portletConfiguration = new PortletPreferencesConfiguration(preferences);
			else
				portletConfiguration = new PortletPreferencesConfiguration(new PortalPreferences(account));
		}
	}

	public void changePassword() {
		if (changePasswordForm != null) {
			Roma.aspect(FlowAspect.class).popup(changePasswordForm);
		}
	}

	public void changePreferences() {
		if (preferencesForm != null) {
			Roma.aspect(FlowAspect.class).forward(preferencesForm);
		}
	}

	public void configurePortlets() {
		if (portletConfiguration != null) {
			Roma.aspect(FlowAspect.class).forward(portletConfiguration);
		}
	}

	public void logout() {
		ObjectContext.getInstance().logout();
	}

	public void onDispose() {
	}

	public void onShow() {
		PortalPreferences preferences = PortalPreferencesHelper.getUserPreferences(account);
		if (preferences == null) {
			Roma.setFeature(this, "changePreferences", ViewActionFeatures.ENABLED, Boolean.FALSE);
		}
	}
}
