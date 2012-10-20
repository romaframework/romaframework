package org.romaframework.module.users.view.domain.configuration;

import java.util.List;

import org.romaframework.core.Roma;
import org.romaframework.module.users.UsersModule;

public class ConfigurationBaseAccount {

	private UsersModule	usersModule;

	public ConfigurationBaseAccount() {
		usersModule = (UsersModule) Roma.component(UsersModule.class);
	}

	public Integer getPasswordExpiredPeriod() {
		return usersModule.getPasswordPeriod();
	}

	public void setPasswordExpiredPeriod(Integer passwordExpiredPeriod) {
		this.usersModule.setPasswordPeriod(passwordExpiredPeriod);
	}

	public List<String> getRegularExpression() {
		return usersModule.getPasswordMatches();
	}

	public void setRegularExpression(List<String> regularExpression) {
		this.usersModule.setPasswordMatches(regularExpression);
	}

	public Integer getAccountExpiredPeriod() {
		return usersModule.getAccountPeriod();
	}

	public void setAccountExpiredPeriod(Integer accountExpiredPeriod) {
		this.usersModule.setAccountPeriod(accountExpiredPeriod);
	}

	public Integer getPasswordMaximumNumber() {
		return usersModule.getPasswordMaxNumber();
	}

	public void setPasswordMaximumNumber(Integer passwordMaximumNumber) {
		this.usersModule.setPasswordMaxNumber(passwordMaximumNumber);
	}

	public void apply() {

	}
}
