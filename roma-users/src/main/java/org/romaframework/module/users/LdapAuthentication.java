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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.romaframework.aspect.authentication.AuthenticationException;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseAccountStatus;

/**
 * LDAP implementation of the authentication aspect. Allows to use an LDAP (or ActiveDirectory) server for authentication.<br>
 * To replace the default implementation of AuthenticationAspect with this implementation you have to modify
 * &quot;applicationContext-core.xml&quot; and replace the bean named &quot;AuthenticationAspect&quot; with the following: <br>
 * <br>
 * <code>
 * 	&lt;bean id="AuthenticationAspect" class="org.romaframework.module.users.LdapAuthentication" singleton="true"&gt;<br>
 * 	&lt;property name="domain" value="&lt;domain-name&gt;" /&gt;<br>
 * 	&nbsp;&lt;property name="ldapHost" value="ldap://&lt;host-name&gt;" /&gt;<br>
 * 	&nbsp;&nbsp;&lt;property name="searchBase" value="your AD root, e.g. dc=mydomain,dc=org" /&gt;<br>
 * 	&nbsp;&lt;property name="singleSessionPerUser" value="false" /&gt;<br>
 * 	&nbsp;&lt;property name="accountBinder"&gt;<br>
 * 	&nbsp;&nbsp;&lt;bean class="org.romaframework.module.users.SimpleAccountBinder"/&gt;<br>
 * 	&nbsp;&nbsp;&lt;!-- override this for a new strategy of binding an LDAP account to a BaseAccount --&gt;<br>
 * 	&nbsp;&lt;/property&gt;<br>
 * 	&lt;/bean&gt;<br>
 * </code>
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public class LdapAuthentication extends UsersAuthentication {

	/**
	 * users that have to be authenticated with basic {@link UsersAuthentication}
	 */
	protected List<String>	nonLdapUsers	= new ArrayList<String>();

	/**
	 * your domain name
	 */
	protected String				domain;

	/**
	 * ldap://&lt;your AD controller&gt;
	 */
	protected String				ldapHost;

	/**
	 * your AD root e.g. dc=mydomain,dc=org
	 */
	protected String				searchBase;

	protected AccountBinder	accountBinder;

	protected List<String>	returnedAttributes;

	@Override
	public Object authenticate(String iUserName, String iUserPasswd, Map<String, String> iParameters) throws AuthenticationException {

		if (nonLdapUsers != null && nonLdapUsers.contains(iUserName)) {
			return super.authenticate(iUserName, iUserPasswd, iParameters);
		}

		Map<?, ?> authenticationResult = authenticateLdap(iUserName, iUserPasswd);
		BaseAccount account = null;
		AccountBinder binder = accountBinder;
		if (binder == null) {
			binder = new SimpleAccountBinder();
		}
		if (authenticationResult != null) {
			account = binder.getAccount(iUserName, authenticationResult);
		}

		if (account == null) {
			throwException("Authentication failed");
		}
		QueryByFilter byFilter = new QueryByFilter(BaseAccountStatus.class);
		byFilter.addItem("name", QueryByFilter.FIELD_EQUALS, UsersInfoConstants.STATUS_ACTIVE);
		BaseAccountStatus accountStatus = Roma.context().persistence().queryOne(byFilter);
		if (account.getStatus() == null || !account.getStatus().equals(accountStatus))
			throwException("Account " + iUserName + " is not active");

		if (isSingleSessionPerUser()) {
			dropExistingSessions(account);
		}

		Roma.session().getActiveSessionInfo().setAccount(account);

		return account;
	}

	public AccountBinder getAccountBinder() {
		return accountBinder;
	}

	public void setAccountBinder(AccountBinder accountBinder) {
		this.accountBinder = accountBinder;
	}

	protected Map<?, ?> authenticateLdap(String user, String pass) {

		String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";

		// Create the search controls
		SearchControls searchCtls = new SearchControls();
		if (!(returnedAttributes == null) && returnedAttributes.size() > 0) {
			searchCtls.setReturningAttributes(returnedAttributes.toArray(new String[0]));
		}
		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapHost);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
		env.put(Context.SECURITY_CREDENTIALS, pass);

		LdapContext ctxGC = null;

		try {
			ctxGC = new InitialLdapContext(env, null);
			// Search objects in GC using filters
			NamingEnumeration<?> answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				Map<String, Object> amap = null;
				if (attrs != null) {
					amap = new HashMap<String, Object>();
					NamingEnumeration<?> ne = attrs.getAll();
					while (ne.hasMore()) {
						Attribute attr = (Attribute) ne.next();
						amap.put(attr.getID(), attr.get());
					}
					ne.close();
				}
				return amap;
			}
		} catch (NamingException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLdapHost() {
		return ldapHost;
	}

	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public String getSearchBase() {
		return searchBase;
	}

	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}

	public List<String> getReturnedAttributes() {
		return returnedAttributes;
	}

	public void setReturnedAttributes(List<String> returnedAttributes) {
		this.returnedAttributes = returnedAttributes;
	}

	public List<String> getNonLdapUsers() {
		return nonLdapUsers;
	}

	public void setNonLdapUsers(List<String> nonLdapUsers) {
		this.nonLdapUsers = nonLdapUsers;
	}

}
