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
package org.romaframework.frontend.view.domain.activesession;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.form.SelectableInstance;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageChat;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.domain.message.MessageTextEdit;
import org.romaframework.frontend.domain.message.MessageYesNo;
import org.romaframework.frontend.view.domain.RomaControlPanelTab;

@CoreClass(orderFields = "info sessions", orderActions = "view refresh sendMessage shutdown selectAll deselectAll")
public abstract class ActiveSessionMain extends SelectableInstance implements MessageResponseListener, ViewCallback, RomaControlPanelTab {

	@ViewField(render = "table", position = "block", selectionField = "selection", enabled = AnnotationConstants.FALSE,label="")
	protected List<ActiveSessionListable>	sessions;

	protected int													authenticatedSessions;

	public ActiveSessionMain() {
		sessions = new ArrayList<ActiveSessionListable>();
	}

	protected abstract void fillSessions();

	public void onShow() {
		refresh();
	}

	public void onDispose() {
	}

	@ViewField(enabled = AnnotationConstants.FALSE)
	public int getAuthenticatedSessions() {
		return authenticatedSessions;
	}

	@ViewField(enabled = AnnotationConstants.FALSE)
	public int getTotalSessions() {
		return sessions != null ? sessions.size() : 0;
	}

	public List<ActiveSessionListable> getSessions() {
		return sessions;
	}

	public void view() {
		Object[] sel = getSelection();
		if (sel == null || sel.length == 0)
			return;

		Roma.flow().popup(new ActiveSessionInstance(((ActiveSessionListable) sel[0]).getSession()));
	}

	public void refresh() {
		fillSessions();
	}

	public void shutdown() {
		if (getSelection() == null || getSelection().length == 0)
			return;

		Roma.flow().popup(
				new MessageYesNo("delete", "Warning", this, "Are you sure you want to shutdown the " + getSelection().length + " selected session(s) ?").setIcon("question.gif"));
	}

	public void sendMessage() {
		if (getSelection() == null || getSelection().length == 0)
			return;

		Roma.flow().popup(new MessageTextEdit("message", "Message", this).setIcon("question.png"));
	}

	public void selectAll() {
		Object[] sel = new Object[sessions.size()];
		sessions.toArray(sel);
		setSelection(sel);
		Roma.fieldChanged(this, "sessions");
	}

	public void deselectAll() {
		setSelection(null);
		Roma.fieldChanged(this, "sessions");
	}

	public void responseMessage(Message iMessage, Object iResponse) {
		ActiveSessionListable s;

		if (iMessage instanceof MessageYesNo) {
			if (!(Boolean) iResponse)
				return;

			// FORCE SHUTDOWN OF ALL SELECTED SESSIONS
			for (Object sel : getSelection()) {
				s = (ActiveSessionListable) sel;
				Roma.session().invalidateSession(s.getSession().getSystemSession());
			}

			refresh();
		} else if (iMessage instanceof MessageTextEdit) {
			MessageChat formattedMsg;

			for (Object sel : getSelection()) {
				s = (ActiveSessionListable) sel;

				formattedMsg = new MessageChat("system message", "System message", Roma.session().getActiveSessionInfo(), s.getSession(), ((MessageTextEdit) iMessage).getDetail());

				Roma.flow().forward(formattedMsg, "popup", null, s.getSession());
			}
		}
	}

}
