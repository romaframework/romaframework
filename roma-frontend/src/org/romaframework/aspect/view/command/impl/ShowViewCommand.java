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

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.command.ViewCommand;
import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.screen.Screen;

public class ShowViewCommand implements ViewCommand {
	private SessionInfo	session;
	private Screen			screen;
	private ContentForm	form;
	private String			where;

	public ShowViewCommand(SessionInfo iSession, Screen iScreen, ContentForm iForm, String iWhere) {
		session = iSession;
		screen = iScreen;
		form = iForm;
		where = iWhere;
	}

	public SessionInfo getSession() {
		return session;
	}

	public ContentForm getForm() {
		return form;
	}

	public String getWhere() {
		return where;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
