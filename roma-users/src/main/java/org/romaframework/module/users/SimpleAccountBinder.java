/*
 * Copyright 2009 Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.module.users;

import java.util.Map;

import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.repository.BaseAccountRepository;

/**
 * Default implementation of {@link AccountBinder}. Binds an LDAP user to a BaseAccount
 * with the same name. Requires a BaseAccount in the database for each LDAP user
 * @author Luigi Dell'Aquila
 *
 */
public class SimpleAccountBinder implements AccountBinder{

	public BaseAccount getAccount(String authenticationName, Map<?,?> authenticationResult) {
		return Roma.component(BaseAccountRepository.class).findByName(authenticationName);
	}

}
