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
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;

public class Message implements ViewCallback {

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String									id;
	@ViewField(visible = AnnotationConstants.FALSE)
	protected String									title;
	@ViewField(visible = AnnotationConstants.FALSE)
	protected MessageResponseListener	listener;

	public Message() {
	}

	public Message(String iId, String iTitle, MessageResponseListener iListener) {
		id = iId;
		title = Roma.i18n().resolve(iTitle);
		listener = iListener;
	}

	public void onShow() {
		Roma.setFeature(this, ViewFieldFeatures.LABEL, title);
	}

	public void onDispose() {
	}

	public MessageResponseListener getListener() {
		return listener;
	}

	public void setResponse(Object iResponse) {
		if (listener != null)
			listener.responseMessage(this, iResponse);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void close() {
		Roma.flow().back();
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setListener(MessageResponseListener listener) {
		this.listener = listener;
	}

}
