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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Manage multiple ErrorReport.
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class ComposedErrorReporter implements ErrorReporter {
	private List<ErrorReporter>	reporters	= new ArrayList<ErrorReporter>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.frontend.domain.message.ErrorReporter#reportError(java.lang.String, java.lang.Throwable)
	 */
	public void reportError(String message, Throwable exception) {
		for (ErrorReporter errorReporter : reporters) {
			errorReporter.reportError(message, exception);
		}
	}

	public List<ErrorReporter> getReporters() {
		return reporters;
	}

	public void setReporters(List<ErrorReporter> reporters) {
		this.reporters = reporters;
	}

	public void addErrorReporter(ErrorReporter errorReporter) {
		this.reporters.add(errorReporter);
	}
}
