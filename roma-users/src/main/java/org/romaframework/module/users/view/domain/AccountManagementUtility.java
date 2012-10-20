package org.romaframework.module.users.view.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.romaframework.core.Roma;
import org.romaframework.module.users.UsersModule;
import org.romaframework.module.users.domain.BaseAccount;

/**
 * Utility class for the management of account and password through Spring injection Check of the password expiration period Check
 * for matching between password and regular expression Check of the account expiration period Check for previous password
 * 
 * @author F.Cuda
 * 
 */
public class AccountManagementUtility {

	public static final long	DAY_MILLISECONDS	= 86400000; // 1000 * 60 * 60 * 24

	/**
	 * Check if the password period has gone off
	 * 
	 * @return
	 */
	public static boolean isPasswordExpired(BaseAccount account) {
		Integer period = Roma.component(UsersModule.class).getPasswordPeriod();
		if (period != null) {
			// current Date minus dateSignedOn in day
			if (Math.round(((new Date()).getTime() - account.getLastPasswordUpdate().getTime()) / DAY_MILLISECONDS) > period)
				return true;
		}
		return false;
	}

	/**
	 * Check if the account period has gone off
	 * 
	 * @param account
	 * @return
	 */
	public static boolean isAccountExpired(BaseAccount account) {
		Integer period = Roma.component(UsersModule.class).getAccountPeriod();
		if (period != null && account.getSignedOn() != null) {
			// current Date minus dateSignedOn in day
			if (Math.round(((new Date()).getTime() - account.getSignedOn().getTime()) / DAY_MILLISECONDS) > period)
				return true;
		}
		return false;
	}

	/**
	 * Methods allow you to match new password with regular expression setted inside BaseAccount
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public static boolean isPasswordMathedRegExpression(String password) {
		List<String> regExpression = Roma.component(UsersModule.class).getPasswordMatches();
		if (regExpression != null) {
			for (String curReg : regExpression)
				if (!curReg.equals("")) {
					if (!password.matches(curReg))
						return false;
				}
		}
		return true;
	}

	/**
	 * Discover if the password have been used before
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public static boolean isPasswordUnused(BaseAccount account, String password) {

		Integer passwordMaxNumber = Roma.component(UsersModule.class).getPasswordMaxNumber();

		if (passwordMaxNumber != null) {
			List<String> oldPwd = account.getOldPasswords();
			if (oldPwd == null) {
				oldPwd = new ArrayList<String>();
				account.setOldPasswords(oldPwd);
			}
			if (oldPwd.contains(password))
				return false;
			oldPwd.add(password);
			if (oldPwd.size() > passwordMaxNumber)
				oldPwd.remove(0);
		}
		return true;
	}
}
