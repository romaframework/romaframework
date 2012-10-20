/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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

package org.romaframework.aspect.session.html;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.session.SessionListener;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.web.session.HttpAbstractSessionAspect;

public class HttpSessionAspect extends HttpAbstractSessionAspect {

	private static final String	CURRENT_LOCALE					= "HttpSessionAspect_current_locale";
	
	public HttpSessionAspect() {
		super();
		sessions = Collections.synchronizedMap(new HashMap<String, SessionInfo>());
	}

	// TODO
	// public Object getActiveSystemSession() {
	// // GET THREAD'S CURRENT CONTEXT
	// return HtmlSessionHelper.getInstance().getActiveSession();
	// }

	public SessionInfo getActiveSessionInfo() {
		// GET THREAD'S CURRENT CONTEXT
		final HttpSession httpSession = (HttpSession) getActiveSystemSession();// HtmlSessionHelper.getInstance().getActiveSession();
		if (httpSession == null) {
			return null;
		} else {
			// RETURN SESSION INFO, IF ANY
			return sessions.get(httpSession.getId());
		}
	}

	public SessionInfo addSession(final Object iSession) {
		// CREATE ACTIVE SESSION OBJECT
		final SessionInfo activeSession = new SessionInfo(iSession);

		activeSession.setSystemSession(iSession);
		synchronized (sessions) {
			sessions.put(((HttpSession) activeSession.getSystemSession()).getId(), activeSession);
		}

		if (log.isDebugEnabled()) {
			log.debug("[HttpSessionAspect.addSession] User session created id='" + ((HttpSession) iSession).getId() + "'");
		}

		final List<SessionListener> listeners = Controller.getInstance().getListeners(SessionListener.class);

		synchronized (listeners) {
			for (final SessionListener listener : listeners) {
				listener.onSessionCreating(activeSession);
			}
		}

		return activeSession;
	}

	public Locale getActiveLocale() {
		try {
			Locale result = (Locale) Roma.session().getProperty(CURRENT_LOCALE);
			if (result == null) {
				HttpServletRequest request = ObjectContext.getInstance().getContextComponent(HttpAbstractSessionAspect.CONTEXT_REQUEST_PAR);
				result = request.getLocale();
			}
			return result;
		} catch (Exception e) {
			return Locale.getDefault();
		}
	}

	public void setActiveLocale(Locale locale) {
		Roma.session().setProperty(CURRENT_LOCALE, locale);
	}

	public Object getUnderlyingComponent() {
		return null;
	}

	
	
}
