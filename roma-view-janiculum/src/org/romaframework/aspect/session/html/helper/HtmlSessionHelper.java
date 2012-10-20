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
package org.romaframework.aspect.session.html.helper;

import javax.servlet.http.HttpSession;

public final class HtmlSessionHelper {

	private static final String	CURRENT_SESSION_STARTED	= "HttpSessionAspect_current_session_started";

	public static boolean isStarted(final HttpSession session) {
		if (session == null) {
			return false;
		}
		final Object started = session.getAttribute(CURRENT_SESSION_STARTED);
		return Boolean.TRUE.equals(started);
	}

	public static void setStarted(final HttpSession session) {
		if (session == null) {
			return;
		}
		session.setAttribute(CURRENT_SESSION_STARTED, Boolean.TRUE);
	}
}
