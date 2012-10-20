/*
 *
 * Copyright 2008 Giordano Maestro (giordano.maestro--at--assetdata.it)
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

import java.security.Principal;

/**
 * Base abstract class representing an account in the system. Current direct implementations are BaseAccount and BaseGroup. This
 * generalization allows to assign privileges to a single account or to a group of it.
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public abstract class AbstractAccount implements Principal {

	protected String	name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
