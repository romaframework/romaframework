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

package org.romaframework.module.users.view.domain.baseaccount;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.romaframework.aspect.authentication.AuthenticationAspect;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.validation.CustomValidation;
import org.romaframework.aspect.validation.MultiValidationException;
import org.romaframework.aspect.validation.ValidationException;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.module.users.UsersInfoConstants;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseAccountStatus;
import org.romaframework.module.users.domain.BaseGroup;
import org.romaframework.module.users.repository.BaseAccountStatusRepository;
import org.romaframework.module.users.view.domain.AccountManagementUtility;
import org.romaframework.module.users.view.domain.basegroup.BaseGroupSelectBox;

public class BaseAccountInstance extends CRUDInstance<BaseAccount> implements CustomValidation {
	private List<BaseAccountStatus>	statuses;

	@ViewField(render = "password")
	@ValidationField(required = AnnotationConstants.TRUE)
	protected String								confirmPassword;

	protected boolean								logoutAfterSave;

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED)
	protected BaseGroupSelectBox		groups;

	@ViewField(render = "password")
	protected String								password;

	@Override
	public void onShow() {
		super.onShow();
		statuses = Roma.component(BaseAccountStatusRepository.class).getAll();
		Roma.fieldChanged(this, "statuses");
	}

	@Override
	public void onCreate() {
		BaseAccount newAccount =new BaseAccount();
		setEntity(newAccount);
		getEntity().setSignedOn(new Date());
		getEntity().setLastModified(new Date());
		getEntity().setGroups(new HashSet<BaseGroup>());
		try {
			groups = new BaseGroupSelectBox(getEntity(), "groups");
		} catch (IllegalArgumentException iae) {
		}
		Roma.fieldChanged(this, "groups");
		QueryByFilter qbf = new QueryByFilter(BaseAccountStatus.class);
		qbf.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		qbf.addItem("name", QueryByFilter.FIELD_EQUALS, UsersInfoConstants.STATUS_ACTIVE);

		getEntity().setStatus((BaseAccountStatus) Roma.context().persistence().queryOne(qbf));
		Roma.fieldChanged(entity, "status");
	}

	@Override
	public void onRead() {
		confirmPassword = getEntity().getPassword();
		password = getEntity().getPassword();
		Roma.fieldChanged(this, "confirmPassword");
		try {
			groups = new BaseGroupSelectBox(getEntity(), "groups");
		} catch (IllegalArgumentException iae) {
		}
		Roma.fieldChanged(this, "groups");
	}

	@Override
	public void onUpdate() {
		confirmPassword = getEntity().getPassword();
		password = getEntity().getPassword();
		Roma.fieldChanged(this, "confirmPassword");
		BaseAccount current = (BaseAccount) Roma.aspect(AuthenticationAspect.class).getCurrentAccount();
		logoutAfterSave = current.equals(entity);
		try {
			groups = new BaseGroupSelectBox(getEntity(), "groups");
		} catch (IllegalArgumentException iae) {
		}
		Roma.fieldChanged(this, "groups");
	}

	public void validate() throws ValidationException {
		MultiValidationException exs = new MultiValidationException();
		if (getPassword()!=null && !getPassword().equals(confirmPassword)) {
			exs.addException(new ValidationException(this, "confirmPassword", "$change.error", null));
		}

		if (!AccountManagementUtility.isPasswordMathedRegExpression(getPassword())) {
			exs.addException(new ValidationException(this, "password", "$invalidPassword.error", null));
		}
		String encryptedPassword;
		try {
			encryptedPassword = Roma.aspect(AuthenticationAspect.class).encryptPassword(getPassword());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!AccountManagementUtility.isPasswordUnused(getEntity(), encryptedPassword)) {
			exs.addException(new ValidationException(this, "password", "$alreadyUsed.error", null));
		}
		if (exs.hasExceptions()) {
			throw exs;
		}
	}

	@ViewField(label = "$status", render = "select", selectionField = "entity.status")
	public List<BaseAccountStatus> getStatuses() {
		return statuses;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) throws NoSuchAlgorithmException {
		this.confirmPassword = confirmPassword;

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BaseGroupSelectBox getGroups() {
		return groups;
	}

	public void setGroups(BaseGroupSelectBox groups) {
		this.groups = groups;
	}

	@Override
	public void save() {
		entity.setPassword(password);
		super.save();
		if (logoutAfterSave) {
			ObjectContext.getInstance().logout();
		}
	}
}
