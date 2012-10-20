package org.romaframework.frontend.domain.message;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;

@CoreClass(orderFields = "icon history message")
@ViewClass(label = "Chat")
public class MessageChat extends Message {
	@ViewField(render = ViewConstants.RENDER_TEXTAREA, enabled = AnnotationConstants.FALSE)
	protected String			history;

	@ViewField(render = ViewConstants.RENDER_TEXTAREA)
	protected String			message;

	protected SessionInfo	sender;
	protected SessionInfo	receiver;

	public MessageChat(String id, String title, SessionInfo iSender, SessionInfo iReceiver, String iMessage) {
		super(id, title, null);
		sender = iSender;
		receiver = iReceiver;
		String account = sender.getAccount() != null ? sender.getAccount().toString() : "";
		history = iMessage != null ? "\n---- FROM " + account + " ----\n" + iMessage : "";
	}

	public String getHistory() {
		return history;
	}

	public String getMessage() {
		return message;
	}

	public void send() {
		MessageChat chat = new MessageChat("system message", "System message", receiver, sender, message);

		history = "\n---- ME ----\n" + message + "\n" + history;
		Roma.fieldChanged(this, "history");
		message = "";
		Roma.fieldChanged(this, "message");

		Roma.aspect(FlowAspect.class).forward(chat, "popup", null, receiver);
	}
}
