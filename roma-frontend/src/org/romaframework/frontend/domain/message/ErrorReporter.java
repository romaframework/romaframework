/*
 * Copyright 2009 F.Cuda 
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

/**
 * Individual listener of Event
 * 
 * @author F.Cuda
 * 
 */
public interface ErrorReporter {

	/**
	 * Report an application error.
	 * 
	 * @param message
	 *          to report
	 * @param exception
	 *          to report.
	 */
	public void reportError(String message, Throwable exception);

}
