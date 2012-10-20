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

import org.romaframework.aspect.core.annotation.CoreClass;

@CoreClass(orderFields = "name notes")
public class BaseGroup extends AbstractAccount implements Serializable {

	private static final long	serialVersionUID	= 6786088888101803520L;

	protected String					notes;

	public BaseGroup() {

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BaseGroup)) {
			return false;
		}

		BaseGroup other = (BaseGroup) obj;
		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		if (name == null)
			return super.hashCode();
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
