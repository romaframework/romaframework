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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.validation.annotation.ValidationField;

public class BaseFunction implements Serializable {
	
	private static final long	serialVersionUID	= -4636425313303024120L;

	@ValidationField(required = AnnotationConstants.TRUE)
	protected String	name;

	protected boolean	allow;

	public BaseFunction() {
		allow = false;
	}

	public BaseFunction(String name, boolean allow) {
		this.name = name;
		this.allow = allow;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BaseFunction))
			return false;

		if (this == obj)
			return true;

		BaseFunction other = (BaseFunction) obj;
		if (name == null || other.name == null)
			return false;

		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public String toString() {
		return name != null ? name + " = " + (allow ? "ALLOWED" : "NOT ALLOWED") : "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAllow() {
		return allow;
	}

	public void setAllow(boolean allow) {
		this.allow = allow;
	}
}
