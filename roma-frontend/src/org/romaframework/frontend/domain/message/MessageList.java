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

package org.romaframework.frontend.domain.message;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.config.Destroyable;

public class MessageList extends MessageOk implements Destroyable {

	public MessageList(String iTitle) {
		super(iTitle, null);
		messages = new ArrayList<String>();
	}

	/**
	 * Delete all messages
	 */
	public void destroy() {
		messages.clear();
	}

	public List<String> getMessages() {
		return messages;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addMessage(String iMessage) {
		messages.add(iMessage);
	}

	@ViewField(render = "rowset", position = ViewConstants.LAYOUT_BLOCK)
	private List<String>	messages;
}
