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

package org.romaframework.module.users;

import java.util.List;

import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.schema.SchemaClassResolver;

public class UsersModule extends SelfRegistrantModule {

	protected Integer passwordPeriod;
	protected List<String> passwordMatches;
	protected Integer accountPeriod;
	protected Integer passwordMaxNumber;

	public String moduleName() {
		return "users";
	}

	public void startup() {
		// REGISTER THE APPLICATION DOMAIN
		Roma.component(SchemaClassResolver.class).addDomainPackage(
				UsersModule.class.getPackage().getName());

		// REGISTER THE APPLICATION DOMAIN + VIEW
		Roma.component(SchemaClassResolver.class).addDomainPackage(
				UsersModule.class.getPackage().getName()
						+ Utility.PACKAGE_SEPARATOR + ViewAspect.ASPECT_NAME);

		status = STATUS_UP;
	}

	public void shutdown() {
		status = STATUS_DOWN;
	}

	public Integer getPasswordPeriod() {
		return passwordPeriod;
	}

	public void setPasswordPeriod(Integer passwordPeriod) {
		this.passwordPeriod = passwordPeriod;
	}

	
	public List<String> getPasswordMatches() {
		return passwordMatches;
	}

	public void setPasswordMatches(List<String> regExpression) {
		this.passwordMatches = regExpression;
	}

	public Integer getAccountPeriod() {
		return accountPeriod;
	}

	public void setAccountPeriod(Integer accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	public Integer getPasswordMaxNumber() {
		return passwordMaxNumber;
	}

	public void setPasswordMaxNumber(Integer passwordMaxNumber) {
		this.passwordMaxNumber = passwordMaxNumber;
	}

}
