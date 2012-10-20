/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.module.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseAccountStatus;
import org.romaframework.module.users.domain.BaseProfile;
import org.romaframework.module.users.domain.BaseProfile.Mode;
import org.romaframework.module.users.repository.BaseProfileRepository;

/**
 * @author l.molino
 */
public class UsersHelper {

	private static UsersHelper	instance	= new UsersHelper();

	public UsersHelper() {
	}

	public List<BaseProfile> getProfileList() {
		return Roma.component(BaseProfileRepository.class).getAll();
	}

	public BaseProfile[] getProfileArray() {
		List<BaseProfile> profiles = getProfileList();
		BaseProfile[] profileArray = new BaseProfile[profiles.size()];
		if (profiles.size() > 0) {
			profiles.toArray(profileArray);
		}
		return profileArray;
	}

	public BaseProfile getProfile(String iName) {
		QueryByFilter query = new QueryByFilter(BaseProfile.class);
		query.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		if (iName != null) {
			query.addItem("name", QueryByFilter.FIELD_EQUALS, iName);
		}
		return Roma.context().persistence().queryOne(query);
	}

	public BaseProfile setProfile(String iName, Mode iMode) {
		BaseProfile profile = new BaseProfile(iName, null, iMode, "HomePage");
		return setProfile(profile);
	}

	public BaseProfile setProfile(BaseProfile iProfile) {
		List<BaseProfile> profiles = getProfileList();
		if (!profiles.contains(iProfile)) {
			iProfile = Roma.context().persistence().createObject(iProfile);
		}
		return iProfile;
	}

	public List<BaseAccount> getAccountList(String iProfileName) {
		return getAccountList(getProfile(iProfileName));
	}

	public List<BaseAccount> getAccountList(BaseProfile iProfile) {
		List<BaseAccount> result;
		QueryByFilter filter = new QueryByFilter(BaseAccount.class);
		filter.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		if (iProfile != null) {
			filter.addItem("profile", QueryByFilter.FIELD_EQUALS, iProfile);
		}
		result = Roma.context().persistence().query(filter);
		return result;
	}

	public BaseAccount getAccount(String iProfileName, String iName) {
		return getAccount(getProfile(iProfileName), iName);
	}

	public BaseAccount getAccount(BaseProfile iProfile, String iName) {
		QueryByFilter filter = new QueryByFilter(BaseAccount.class);
		filter.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		if (iProfile != null) {
			filter.addItem("profile", QueryByFilter.FIELD_EQUALS, iProfile);
		}
		filter.addItem("name", QueryByFilter.FIELD_EQUALS, iName);
		return Roma.context().persistence().queryOne(filter);
	}

	public BaseAccount setAccount(String iProfileName, String iName, String iPassword) throws NoSuchAlgorithmException {
		return setAccount(getProfile(iProfileName), iName, iPassword, null);
	}

	public BaseAccount setAccount(BaseProfile iProfile, String iName, String iPassword) throws NoSuchAlgorithmException {
		return setAccount(iProfile, iName, iPassword, null);
	}

	public BaseAccount setAccount(String iProfileName, String iName, String iPassword, BaseAccountStatus iStatus) throws NoSuchAlgorithmException {
		return setAccount(getProfile(iProfileName), iName, iPassword, iStatus);
	}

	public BaseAccount setAccount(BaseProfile iProfile, String iName, String iPassword, BaseAccountStatus iStatus) throws NoSuchAlgorithmException {
		if (iStatus == null) {
			QueryByFilter byFilterAct = new QueryByFilter(BaseAccountStatus.class);
			byFilterAct.addItem("name", QueryByFilter.FIELD_EQUALS, UsersInfoConstants.STATUS_ACTIVE);
			iStatus = Roma.context().persistence().queryOne(byFilterAct);
		}
		BaseAccount iAccount = new BaseAccount();
		iAccount.setName(iName);
		iAccount.setPassword(iPassword);
		iAccount.setProfile(iProfile);
		iAccount.setStatus(iStatus);
		return storeAccount(iAccount);
	}

	public BaseAccount setAccount(BaseAccount iAccount) {
		return storeAccount(iAccount);
	}

	private BaseAccount storeAccount(BaseAccount iAccount) {
		List<BaseAccount> result = getAccountList(iAccount.getProfile());
		if (!result.contains(iAccount)) {
			iAccount = Roma.context().persistence().createObject(iAccount);
		}
		return iAccount;
	}

	public static UsersHelper getInstance() {
		return instance;
	}
}
