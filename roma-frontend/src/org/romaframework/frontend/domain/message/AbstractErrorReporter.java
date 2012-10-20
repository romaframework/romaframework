/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
package org.romaframework.frontend.domain.message;

import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.Roma;
import org.romaframework.core.config.ApplicationConfiguration;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public abstract class AbstractErrorReporter implements ErrorReporter {

	public AbstractErrorReporter() {
		if (Roma.existComponent(ComposedErrorReporter.class)) {
			Roma.component(ComposedErrorReporter.class).addErrorReporter(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.frontend.domain.message.ErrorReporter#reportError(java.lang.String, java.lang.Throwable)
	 */
	public void reportError(String message, Throwable exception) {

		Map<String, Stack<Object>> flowStack = Roma.flow().getHistory();
		ApplicationConfiguration conf = Roma.component(ApplicationConfiguration.class);
		reportError(conf.getApplicationName(), conf.getApplicationVersion(), Roma.session().getActiveSessionInfo(), flowStack, message, exception);
	}

	/**
	 * Report Error implementation with user,session,application,exception information.
	 * 
	 * @param aplicationName
	 *          the name of application.
	 * @param version
	 *          the version of application.
	 * @param sessionInfo
	 *          the current session information.
	 * @param flowStack
	 *          the FlowAspet stack.
	 * @param message
	 *          the error message
	 * @param exception
	 *          to report.
	 */
	protected abstract void reportError(String aplicationName, String version, SessionInfo sessionInfo, Map<String, Stack<Object>> flowStack, String message, Throwable exception);
}
