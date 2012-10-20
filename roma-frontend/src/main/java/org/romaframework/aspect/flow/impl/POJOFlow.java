/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.flow.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.flow.FlowAspectAbstract;
import org.romaframework.aspect.flow.feature.FlowActionFeatures;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.message.MessageYesNo;

/**
 * POJO based implementation of Flow Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class POJOFlow extends FlowAspectAbstract {
	private static final String	INHIBIT_CONFIRM_ACTION	= "pojoflow_inhibitConfirm";
	public static final String	SESS_PROPERTY_HISTORY		= "_HISTORY_";
	protected SessionAspect			sessionAspect;
	protected ViewAspect				viewAspect;

	public Map<String, Object> current(SessionInfo iSession) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Stack<Object>> stack = getHistory(iSession);
		for (Map.Entry<String, Stack<Object>> entry : stack.entrySet()) {
			result.put(entry.getKey(), entry.getValue().peek());
		}
		return result;
	}

	public Object current(String area, SessionInfo iSession) {
		Map<String, Stack<Object>> stack = getHistory(iSession);
		if (area == null)
			area = getScreen(iSession).getActiveArea();
		Stack<Object> areaStack = stack.get(area);
		if (areaStack == null || areaStack.isEmpty()) {
			return null;
		}
		return areaStack.peek();
	}

	private Screen getScreen(SessionInfo iSession) {
		if (iSession == null) {
			return Roma.view().getScreen();
		}
		return Roma.view().getScreen(iSession);
	}

	public void forward(SchemaClass iNextClass, String iPosition) {
		forward(iNextClass, iPosition, null, null);
	}

	public void forward(Object iNextObject, String iPosition, Screen iScreen, SessionInfo iSession) {
		if (iNextObject == null)
			return;

		if (iNextObject instanceof String) {
			SchemaClass cls = Roma.schema().getSchemaClass((String) iNextObject);
			if (cls == null)
				return;

			// SEARCH THE FORM INSTANCE BETWEEN USER SESSSION FORMS
			iNextObject = Roma.session().getObject(cls);
		} else if (iNextObject instanceof Class<?>) {
			SchemaClass cls = Roma.schema().getSchemaClass((Class<?>) iNextObject);
			if (cls == null)
				return;

			// SEARCH THE FORM INSTANCE BETWEEN USER SESSSION FORMS
			iNextObject = Roma.session().getObject(cls);
		} else if (iNextObject instanceof SchemaClass) {
			SchemaClass cls = ((SchemaClass) iNextObject);

			// SEARCH THE FORM INSTANCE BETWEEN USER SESSSION FORMS
			iNextObject = Roma.session().getObject(cls);
		}

		moveForward(iSession, iNextObject, iPosition);

		// SHOW THE FORM
		viewAspect.show(iNextObject, iPosition, iScreen, iSession);
	}

	public void clearHistory(SessionInfo iSession) {
		getHistory(iSession).clear();
	}

	@Override
	public Object back(String area, SessionInfo iSession) {
		Object currentObject = current(area, iSession);
		if (currentObject == null)
			return null;
		Object backObject = moveBack(area, iSession);
		viewAspect.show(backObject, area, null, iSession);
		return backObject;
	}

	protected void moveForward(SessionInfo iSession, Object iNextObject, String iPosition) {
		if (iPosition == null)
			iPosition = getScreen(iSession).getActiveArea();
		else {
			if (iPosition.startsWith("screen:"))
				iPosition = iPosition.substring("screen:".length());
			while (iPosition.startsWith("/"))
				iPosition = iPosition.substring(1);
			if (iPosition.contains(":"))
				iPosition = iPosition.substring(0, iPosition.indexOf(":"));
		}
		Stack<Object> history = getAreaHistory(iSession, iPosition);

		if (!history.isEmpty()) {
			Object last = history.peek();
			if (last.equals(iNextObject))
				// SAME OBJECT: JUST A REFRESH, DON'T STORE IN HISTORY
				return;
		}

		history.push(iNextObject);
	}

	protected Object moveBack(String area, SessionInfo iSession) {
		getAreaHistory(iSession, area).pop();
		return current(area, iSession);
	}

	public void onAfterAction(Object iContent, SchemaAction iAction, Object returnedValue) {
		Boolean goBack = (Boolean) iAction.getFeature(FlowActionFeatures.BACK);
		if (goBack != null && goBack) {
			back();
			return;
		}

		if (iAction.isSettedFeature(FlowActionFeatures.NEXT)) {
			SchemaClass nextClass = (SchemaClass) iAction.getFeature(FlowActionFeatures.NEXT);
			if (nextClass != null) {
				String nextPosition = (String) iAction.getFeature(FlowActionFeatures.POSITION);
				forward(nextClass, nextPosition);
			}
		}
	}

	public boolean onBeforeAction(Object iContent, SchemaAction iAction) {
		if (!(Boolean.TRUE.equals(iAction.getFeature(FlowActionFeatures.CONFIRM_REQUIRED)))) {
			return true;
		}
		if (Boolean.TRUE.equals(Roma.context().component(INHIBIT_CONFIRM_ACTION))) {
			return true;
		}

		String confirmMessage = (String) iAction.getFeature(FlowActionFeatures.CONFIRM_MESSAGE);
		if (confirmMessage == null) {
			try {
				confirmMessage = Roma.i18n().get(iContent, iAction.getName(), I18NType.CONFIRM, FlowActionFeatures.CONFIRM_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (confirmMessage.startsWith("$")) {
			try {
				confirmMessage = Roma.i18n().resolve(confirmMessage);
			} catch (Exception e) {
			}
		}
		MessageYesNo msg = new MessageYesNo("confirm", "", new ConfirmListener(iAction, iContent), confirmMessage);
		forward(msg, "screen:popup");
		return false;
	}

	public void onExceptionAction(Object iContent, SchemaAction iAction, Exception exception) {
	}

	static class ConfirmListener implements MessageResponseListener {

		protected SchemaClassElement	originalAction;
		protected Object							content;

		protected ConfirmListener(SchemaClassElement iOriginalAction, Object iContent) {
			this.originalAction = iOriginalAction;
			this.content = iContent;
		}

		public void responseMessage(Message iMessage, Object iResponse) {
			if (Boolean.TRUE.equals(iResponse)) {
				try {
					Roma.context().setComponent(INHIBIT_CONFIRM_ACTION, true);
					((SchemaAction) originalAction).invoke(content);
					Roma.context().setComponent(INHIBIT_CONFIRM_ACTION, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map<String, Stack<Object>> getHistory() {
		return getHistory(null);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Stack<Object>> getHistory(SessionInfo iSession) {
		Map<String, Stack<Object>> history = (Map<String, Stack<Object>>) sessionAspect.getProperty(iSession, SESS_PROPERTY_HISTORY);
		if (history == null) {
			history = new HashMap<String, Stack<Object>>();
			sessionAspect.setProperty(SESS_PROPERTY_HISTORY, history);
		}
		return history;
	}

	public Stack<Object> getAreaHistory(SessionInfo iSession, String area) {
		Map<String, Stack<Object>> areas = getHistory(iSession);
		if (area == null)
			area = getScreen(iSession).getActiveArea();
		Stack<Object> stack = areas.get(area);
		if (stack == null) {
			stack = new Stack<Object>();
			areas.put(area, stack);
		}
		return stack;
	}

	public void clearHistory() {
		sessionAspect.setProperty(SESS_PROPERTY_HISTORY, null);
	}

	public void clearHistory(String area) {
		getHistory().remove(area);
	}

	@Override
	public void startup() {
		super.startup();
		sessionAspect = Roma.aspect(SessionAspect.ASPECT_NAME);
		viewAspect = Roma.aspect(ViewAspect.ASPECT_NAME);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		sessionAspect = null;
	}

	@Override
	public void popup(Object popup, boolean modal) {
		if (modal) {
			forward(popup, "popup");
		} else {
			forward(popup, "popupNonModal");
		}
	}

	public void configClass(SchemaClassDefinition class1) {
	}

	public void configField(SchemaField field) {
	}

	public Object getUnderlyingComponent() {
		return null;
	}
}
