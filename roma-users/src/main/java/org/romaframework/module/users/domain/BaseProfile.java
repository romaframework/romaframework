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

package org.romaframework.module.users.domain;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.module.users.view.domain.baseprofile.BaseProfileSelect;

public class BaseProfile implements Serializable, Principal {

	private static final long	serialVersionUID	= 2147431210150249521L;

	public enum Mode {
		ALLOW_ALL_BUT("Allow all but"), DENY_ALL_BUT("Deny all but");
		private String	label;

		private Mode(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	};

	protected String										name;
	protected BaseProfile								parent;
	protected Mode											mode;
	protected Map<String, BaseFunction>	functions;
	protected String										homePage;
	protected String										notes;

	public static final byte						MODE_ALLOW_ALL_BUT	= 0;
	public static final byte						MODE_DENY_ALL_BUT		= 1;

	public BaseProfile() {
	}

	public BaseProfile(String iName, BaseProfile iParent, Mode iMode, String iHomePage) {
		name = iName;
		parent = iParent;
		mode = iMode;
		homePage = iHomePage;

		functions = new TreeMap<String, BaseFunction>();
	}

	public void onParent() {
		CRUDHelper.show(BaseProfileSelect.class, this, "parent");
	}

	@Override
	public String toString() {
		return name;
	}

	public Mode getMode() {
		return mode;
	}

	public BaseProfile setMode(Mode iInheritMode) {
		mode = iInheritMode;
		return this;
	}

	public String getName() {
		return name;
	}

	public BaseProfile setName(String name) {
		this.name = name;
		return this;
	}

	public BaseProfile getParent() {
		return parent;
	}

	public BaseProfile setParent(BaseProfile parent) {
		this.parent = parent;
		return this;
	}

	public Map<String, BaseFunction> getFunctions() {
		return functions;
	}

	public BaseProfile setFunctions(Map<String, BaseFunction> functions) {
		this.functions = functions;
		return this;
	}

	public BaseProfile addFunction(String iName, boolean iAllowed) {
		if (functions == null)
			functions = new HashMap<String, BaseFunction>();

		BaseFunction func = new BaseFunction(iName, iAllowed);
		functions.put(iName, func);
		return this;
	}

	public BaseProfile removeFunction(String iKey) {
		functions.remove(iKey);
		return this;
	}

	public String getNotes() {
		return notes;
	}

	public BaseProfile setNotes(String notes) {
		this.notes = notes;
		return this;
	}

	public String getHomePage() {
		return homePage;
	}

	public BaseProfile setHomePage(String homePage) {
		this.homePage = homePage;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final BaseProfile other = (BaseProfile) obj;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
