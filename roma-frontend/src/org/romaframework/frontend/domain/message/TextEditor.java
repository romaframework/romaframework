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

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;

@CoreClass(orderFields = "message icon")
@ViewClass(label = "Editor")
public class TextEditor extends Message {
	@ViewField(label = "", render = ViewConstants.RENDER_TEXTAREA)
	protected String	message;

	public TextEditor(String iId, String iTitle, MessageResponseListener iListener) {
		super(iId, iTitle, iListener);
	}

	public TextEditor(String iId, String iTitle, MessageResponseListener iListener, String iMessage) {
		super(iId, iTitle, iListener);
		message = iMessage;
	}

	public void ok() {
		close();
		setResponse(message);
	}

	public String getMessage() {
		return message;
	}
}
