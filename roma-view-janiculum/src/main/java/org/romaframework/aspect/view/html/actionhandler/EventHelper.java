/*
 * Copyright 2009 Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.view.html.actionhandler;

import java.util.HashSet;
import java.util.Set;

import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.transformer.helper.TransformerHelper;

public class EventHelper {

	private static final int		COMPONENT_ID_INDEX			= 1;
	private static final int		EVENT_NAME_INDEX				= 2;
	private static Set<String>	standardEvents					= new HashSet<String>();

	static {
		standardEvents.add("click");
		standardEvents.add("change");
		standardEvents.add("blur");
		standardEvents.add("dblclick");
		standardEvents.add("focus");
		standardEvents.add("keydown");
		standardEvents.add("keyup");
		standardEvents.add("mousedown");
		standardEvents.add("mousemove");
		standardEvents.add("mouseout");
		standardEvents.add("mouseover");
		standardEvents.add("mouseup");
		standardEvents.add("resize");
	}

	public Set<String> getStandardEvents() {
		return standardEvents;
	}

	public void setEvents(Set<String> events){
		standardEvents.addAll(events);
	}
	
	
	public static String getEventHtmlName(HtmlViewGenericComponent fieldComponent, String eventName) {
		return TransformerHelper.POJO_EVENT_PREFIX + TransformerHelper.SEPARATOR + fieldComponent.getId() + TransformerHelper.SEPARATOR
				+ eventName;
	}

	public static String getComponentId(String eventHtmlName) {
		String[] parts = eventHtmlName.split(TransformerHelper.SEPARATOR);
		return parts[COMPONENT_ID_INDEX];
	}

	public static String getEvent(String eventHtmlName) {
		String[] parts = eventHtmlName.split(TransformerHelper.SEPARATOR);
		if (parts.length == 3) {
			return parts[EVENT_NAME_INDEX];
		} else {
			return null;
		}
	}

}
