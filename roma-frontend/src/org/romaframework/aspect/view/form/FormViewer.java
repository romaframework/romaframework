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

package org.romaframework.aspect.view.form;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.session.SessionListener;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;

/**
 * Manage the current user screen and invoke the custom aspect renderer
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class FormViewer implements SessionListener {

	/**
	 * Contains a map of SessionInfo,
	 */
	private Map<Object, Screen>	userView;
	private SessionAspect				sessionManager;

	private static FormViewer		instance	= new FormViewer();
	private static Log					log				= LogFactory.getLog(FormViewer.class);

	protected FormViewer() {
		sessionManager = Roma.session();
		userView = Collections.synchronizedMap(new HashMap<Object, Screen>());

		Controller.getInstance().registerListener(SessionListener.class, this);
	}

	public Screen getScreen() {
		return getScreen(sessionManager.getActiveSessionInfo());
	}

	public Screen getScreen(Object iSession) {
		return userView.get(iSession);
	}

	/**
	 * Change the current screen.
	 * 
	 * @param iScreen
	 */
	public void setScreen(Screen iScreen) {
		setScreen(iScreen, sessionManager.getActiveSessionInfo());
	}

	/**
	 * Change the current screen.
	 * 
	 * @param iScreen
	 */
	public void setScreen(Screen iScreen, SessionInfo iSession) {
		Screen old = userView.put(iSession, iScreen);
		if (old != null)
			old.clear();
	}

	/**
	 * Render the an object on the defined area
	 * 
	 * @param iArea
	 *          The area where to render the object
	 * @param iForm
	 *          The Object to render
	 */
	public void display(String iArea, Object iForm) {
		Object session = sessionManager.getActiveSessionInfo();

		if (session != null) {
			Screen currentDesktop = userView.get(session);
			display(iArea, iForm, currentDesktop);
		}
	}

	public void display(String iArea, Object iForm, Screen iScreen) {
		if (iScreen == null || Roma.session().getActiveSessionInfo() == null) {
			// SCREEN NOT YET SETTED: PUSH THE FORMS IN THE SESSION TO BE DISPLAYED WHEN THE SCREEN WILL BE SETTED
			LinkedHashMap<String, Object> queuedForms = (LinkedHashMap<String, Object>) Roma.session().getProperty("formQueue");
			if (queuedForms == null) {
				// FIRST TIME: CREATE IT AND PUT IN THE SESSION
				queuedForms = new LinkedHashMap<String, Object>();
				Roma.session().setProperty("formQueue", queuedForms);
			}
			queuedForms.put(iArea, iForm);
		} else {
			sync(iScreen);
			Roma.view().show(iForm, iArea, iScreen, null);
		}
	}

	public void sync() {
		sync(Roma.view().getScreen());
	}

	public void sync(Screen iScreen) {
		LinkedHashMap<String, Object> queuedForms = (LinkedHashMap<String, Object>) Roma.session().getProperty("formQueue");
		if (queuedForms != null) {
			for (Map.Entry<String, Object> entry : queuedForms.entrySet()) {
				Roma.view().show(entry.getValue(), entry.getKey(), iScreen, null);
			}
			queuedForms.clear();
			Roma.session().setProperty("formQueue", null);
		}
	}

	public void onSessionCreating(SessionInfo iSession) {
	}

	/**
	 * Remove user session screen if any
	 */
	public void onSessionDestroying(SessionInfo iSession) {
		Screen screen = userView.remove(iSession);
		if (log.isDebugEnabled())
			log.debug("[FormViewer.onSessionDestroying] Removed screen container: " + screen);

	}

	/**
	 * Get the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static FormViewer getInstance() {
		return instance;
	}
}
