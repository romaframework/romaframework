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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;

@CoreClass(orderFields = "icon message")
@ViewClass(label = "Information")
public class MessageText extends Message {
	@ViewField(label = "", render = "image")
	protected String	icon;

	@ViewField(label = "", render = "html", enabled = AnnotationConstants.FALSE)
	protected String	message;

	public MessageText() {
	}

	public MessageText(String iId, String iTitle) {
		this(iId, iTitle, null);
	}

	public MessageText(String iId, String iTitle, MessageResponseListener iListener) {
		this(iId, iTitle, iListener, null);
	}

	public MessageText(String iId, String iTitle, MessageResponseListener iListener, String iMessage) {
		super(iId, iTitle, iListener);
		setMessage(iMessage);
		setIcon("information.gif");
	}

	public String getMessage() {
		return message;
	}

	public MessageText setMessage(String iMessage) {
		message = Roma.i18n().resolve(iMessage);
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MessageText setIcon(String icon) {
		this.icon = icon;
		return this;
	}
}
