package org.romaframework.module.users.listener;

import org.romaframework.aspect.authentication.LoginListener;
import org.romaframework.core.Roma;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.exception.UserException;
import org.romaframework.module.users.domain.BaseAccount;

public class DefaultLoginListener implements LoginListener {

	public void onError(Throwable t) {
		throw new UserException(null, "$authentication.error", t);
	}

	public void onSuccess() {
		String homePage = ((BaseAccount) Roma.session().getActiveSessionInfo().getAccount()).getProfile().getHomePage();
		if (homePage == null)
			throw new ConfigurationException("$Login.homepage.error");
		Object toDisplay = Roma.session().getObject(homePage);
		Roma.flow().clearHistory();
		Roma.flow().forward(toDisplay);
	}

}
