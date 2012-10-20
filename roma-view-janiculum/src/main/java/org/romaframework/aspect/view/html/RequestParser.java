/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.view.html;

import javax.servlet.http.HttpServletRequest;

import org.romaframework.aspect.view.html.area.HtmlViewRenderable;

public interface RequestParser {

	/**
	 * Parse the request and call the controller for the execution of a method
	 * 
	 * @param request
	 * @return true if the request performed parameter binding, false otherwise
	 * @throws Throwable
	 */
	public boolean parseRequest(HttpServletRequest request) throws Throwable;

	/**
	 * Invoke an event and return the event invoke target.
	 * 
	 * @param request
	 *          with event parameters.
	 * @return the component where was invoked the event.
	 */
	public HtmlViewRenderable invokeEvent(HttpServletRequest request) throws Throwable;
}
