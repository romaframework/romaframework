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

package org.romaframework.aspect.flow;

import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.SchemaActionListener;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageOk;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.message.MessageYesNo;

/**
 * Abstract implementation for Flow Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class FlowAspectAbstract extends SelfRegistrantConfigurableModule<String> implements FlowAspect, SchemaActionListener {

	@Override
	public void startup() {
		Controller.getInstance().registerListener(SchemaActionListener.class, this);
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configAction(SchemaAction iAction) {
	}

	public void configEvent(SchemaEvent iEvent) {
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void forward(Object iNextObject) {
		forward(iNextObject, null);
	}

	public void forward(Object iNextObject, String iPosition) {
		forward(iNextObject, iPosition, null, null);
	}

	public Object back() {
		return back(null, null);
	}

	public Object back(String area) {
		return back(area, null);
	}

	public Object backDefault() {
		return back(Roma.view().getScreen().getDefautlArea());
	}

	public void forwardDefault(Object iNextObject) {
		forward(iNextObject, Roma.view().getScreen().getDefautlArea());
	}

	public Object back(SessionInfo iSession) {
		return back(null, iSession);
	}

	public Object currentDefault() {
		return current(Roma.view().getScreen().getDefautlArea());
	}

	public Map<String, Object> current() {
		return current((SessionInfo) null);
	}

	public Object current(String area) {
		return current(area, null);
	}

	public void popup(Object popup) {
		popup(popup, true);
	}

	public Map<String, Stack<Object>> getHistory() {
		return getHistory(null);
	}

	public void alert(String iTitle, String iBody) {
		alert(iTitle, iBody, null);
	}

	public void alert(String iTitle, String iBody, final AlertListener iListener) {
		if (iListener == null) {
			popup(new MessageOk("", iTitle, null, iBody));
		} else {
			popup(new MessageOk("", iTitle, new MessageResponseListener() {
				public void responseMessage(Message iMessage, Object iResponse) {
					iListener.onAccept();
				}
			}, iBody));
		}
	}

	public void confirm(String iTitle, String iBody, final ConfirmListener iListener) {
		if (iListener == null) {
			popup(new MessageYesNo("", iTitle, null, iBody));
		} else {
			popup(new MessageYesNo("", iTitle, new MessageResponseListener() {
				public void responseMessage(Message iMessage, Object iResponse) {
					iListener.onResponse(Boolean.TRUE.equals(iResponse));
				}
			}, iBody));
		}
	}
}
