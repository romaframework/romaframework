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
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.i18n.annotation.I18nField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;

@CoreClass(orderFields = "message messages")
public class MessageTable extends MessageOk {

	public MessageTable(String iTitle) {
		this(null, iTitle);
	}

	public MessageTable(String iId, String iTitle) {
		super(iId, iTitle);
		messages = new ArrayList<List<?>>();
	}

	@I18nField
	@ViewField(label = "")
	public List<List<?>> getMessages() {
		return messages;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void addMessage(List<?> iMessage) {
		messages.add(iMessage);
	}

	@ViewField(render = "table", enabled = AnnotationConstants.FALSE, position = ViewConstants.LAYOUT_BLOCK)
	private List<List<?>>	messages;
}
