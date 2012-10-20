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

package org.romaframework.module.users.install;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.core.install.AbstractApplicationInstaller;
import org.romaframework.module.users.ActivityLogCategories;
import org.romaframework.module.users.UsersAuthentication;
import org.romaframework.module.users.UsersHelper;
import org.romaframework.module.users.UsersInfoConstants;
import org.romaframework.module.users.domain.ActivityLogCategory;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseAccountStatus;
import org.romaframework.module.users.domain.BaseFunction;
import org.romaframework.module.users.domain.BaseProfile;
import org.romaframework.module.users.repository.ActivityLogCategoryRepository;
import org.romaframework.module.users.repository.BaseAccountRepository;
import org.romaframework.module.users.repository.BaseAccountStatusRepository;

public class UsersApplicationInstaller extends AbstractApplicationInstaller {

	public static final String	PROFILE_ADMINISTRATOR	= "Administrator";
	public static final String	PROFILE_BASIC					= "Basic";
	public static final String	ACCOUNT_ADMIN					= "admin";
	public static final String	ACCOUNT_USER					= "user";
	public static final String	ACCOUNT_SEPARATOR			= ".";

	protected BaseProfile				pAnonymous;
	protected BaseProfile				pAdmin;
	protected BaseProfile				pBasic;
	protected BaseAccountStatus	defStatus;

	public UsersApplicationInstaller() {
	}

	@Override
	public boolean alreadyInstalled() {
		return Roma.component(BaseAccountRepository.class).countByCriteria(new QueryByFilter(BaseAccount.class)) != 0;
	}

	@Override
	public synchronized void install() {

		PersistenceAspect db = Roma.context().persistence();

		createStatuses(db);
		createProfiles();
		try {
			createAccounts();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	public synchronized void install(Object obj) {
		install();
	}

	protected void createStatuses(PersistenceAspect db) {

		BaseAccountStatusRepository repoStatus = Roma.component(BaseAccountStatusRepository.class);
		defStatus = repoStatus.create(new BaseAccountStatus(UsersInfoConstants.STATUS_ACTIVE), PersistenceAspect.STRATEGY_DETACHING);
		repoStatus.create(new BaseAccountStatus(UsersInfoConstants.STATUS_UNACTIVE));
		repoStatus.create(new BaseAccountStatus(UsersInfoConstants.STATUS_SUSPENDED));
		ActivityLogCategoryRepository repoCategory = Roma.component(ActivityLogCategoryRepository.class);
		repoCategory.create(new ActivityLogCategory(ActivityLogCategories.CATEGORY_SYSTEM));
		repoCategory.create(new ActivityLogCategory(ActivityLogCategories.CATEGORY_LOGIN));
		repoCategory.create(new ActivityLogCategory(ActivityLogCategories.CATEGORY_ADMIN));
	}

	protected void createAccounts() throws NoSuchAlgorithmException {
		BaseAccount aAdmin = new BaseAccount();
		aAdmin.setName(ACCOUNT_ADMIN);
		aAdmin.setPassword(ACCOUNT_ADMIN);
		aAdmin.setSignedOn(new Date());
		aAdmin.setStatus(defStatus);
		aAdmin.setLastModified(aAdmin.getSignedOn());
		aAdmin.setProfile(pAdmin);

		UsersHelper.getInstance().setAccount(aAdmin);
		BaseAccount uUser = new BaseAccount();
		uUser.setName(ACCOUNT_USER);
		uUser.setPassword(ACCOUNT_USER);
		uUser.setSignedOn(new Date());
		uUser.setStatus(defStatus);
		uUser.setLastModified(uUser.getSignedOn());
		uUser.setProfile(pBasic);

		UsersHelper.getInstance().setAccount(uUser);
	}

	protected void createProfiles() {
		pAnonymous = new BaseProfile();
		pAnonymous.setName(UsersAuthentication.ANONYMOUS_PROFILE_NAME);
		pAnonymous.setHomePage("HomePage");
		pAnonymous.setFunctions(new HashMap<String, BaseFunction>());
		pAnonymous.setMode(BaseProfile.Mode.ALLOW_ALL_BUT);
		UsersHelper.getInstance().setProfile(pAnonymous);

		pAdmin = new BaseProfile();
		pAdmin.setName(PROFILE_ADMINISTRATOR);
		pAdmin.setHomePage("HomePageAdmin");
		pAdmin.setMode(BaseProfile.Mode.ALLOW_ALL_BUT);
		UsersHelper.getInstance().setProfile(pAdmin);

		pBasic = new BaseProfile();
		pBasic.setName(PROFILE_BASIC);
		pBasic.setHomePage("HomePage");
		pBasic.setMode(BaseProfile.Mode.ALLOW_ALL_BUT);
		UsersHelper.getInstance().setProfile(pBasic);
	}
}
