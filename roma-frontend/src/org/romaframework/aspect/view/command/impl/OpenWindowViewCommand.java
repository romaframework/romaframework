/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.view.command.impl;

import org.romaframework.aspect.view.command.ViewCommand;

public class OpenWindowViewCommand implements ViewCommand {
	protected String	location;
	protected String	name;
	protected String	options;

	public OpenWindowViewCommand(String location) {
		this(location, null, null);
	}

	public OpenWindowViewCommand(String location, String name) {
		this(location, name, null);
	}

	public OpenWindowViewCommand(String location, String name, String options) {
		this.location = location;
		this.name = name;
		this.options = options;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getOptions() {
		return options;
	}
}
