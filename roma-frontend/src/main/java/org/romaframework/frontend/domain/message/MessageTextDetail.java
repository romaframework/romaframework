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
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;

@CoreClass(orderFields = "icon message detail")
@ViewClass(label = "Information")
public class MessageTextDetail extends MessageOk {

	@ViewField(label = "", render = ViewConstants.RENDER_TEXTAREA, enabled = AnnotationConstants.FALSE)
	private String	detail;

	public MessageTextDetail(String iId, String iTitle) {
		this(iId, iTitle, null, null);
	}

	public MessageTextDetail(String iId, String iTitle, MessageResponseListener iListener) {
		this(iId, iTitle, iListener, null);
	}

	public MessageTextDetail(String iId, String iTitle, MessageResponseListener iListener, String iMessage) {
		super(iId, iTitle, iListener, iMessage);
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
