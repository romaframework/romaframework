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

import org.romaframework.module.users.domain.BaseAccount;

/**
 * this interface represents a strategy to associate an LDAP account with a BaseAccount in the application 
 * @author Luigi Dell'Aquila
 *
 */
public interface AccountBinder {

	/**
	 * returns a {@link BaseAccount} corresponding to an LDAP user  
	 * @param authenticationName the LDAP user name
	 * @param authenticationResult the result of the authentication call 
	 * @return the {@link BaseAccount} corresponding to the LDAP user logging in
	 */
	public BaseAccount getAccount(String authenticationName, Map<?,?> authenticationResult);
}
