/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.config.ApplicationConfiguration;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
@CoreClass(orderFields = "icon message customMessage detail")
public class ErrorMessageTextDetail extends MessageTextDetail {

	private Throwable	exception;

	@ViewField(render = ViewConstants.RENDER_TEXTAREA)
	private String		customMessage;

	public ErrorMessageTextDetail(String iId, String iTitle, Throwable exception) {
		this(iId, iTitle, null, null, exception);
	}

	public ErrorMessageTextDetail(String iId, String iTitle, MessageResponseListener iListener, Throwable exception) {
		this(iId, iTitle, iListener, null, exception);
	}

	public ErrorMessageTextDetail(String iId, String iTitle, MessageResponseListener iListener, String iMessage, Throwable exception) {
		super(iId, iTitle, iListener, iMessage);
		this.exception = exception;
	}

	@Override
	public void onShow() {
		super.onShow();
		if (!Roma.existComponent(ErrorReporter.class)) {
			Roma.setFeature(this, "sendReport", ViewActionFeatures.VISIBLE, Boolean.FALSE);
			Roma.setFeature(this, "customMessage", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		} else {
			Roma.setFeature(this, "sendReport", ViewActionFeatures.VISIBLE, Boolean.TRUE);
			Roma.setFeature(this, "customMessage", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
		}
		if (Boolean.FALSE.equals(Roma.component(ApplicationConfiguration.class).isApplicationDevelopment())) {
			Roma.setFeature(this, "detail", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		}
	}

	public void sendReport() {
		try {
			Roma.component(ErrorReporter.class).reportError(getCustomMessage(), exception);
			String msg = Roma.i18n().get("ErrorMessageTextDetail.errorReported");
			MessageOk message = new MessageOk("Error reported", msg, null, msg);
			Roma.flow().popup(message);
		} catch (Exception e) {
			if (Roma.component(ApplicationConfiguration.class).isApplicationDevelopment()) {
				MessageOk message = new MessageOk("Impossible to report error", e.getMessage());
				message.setMessage(e.getMessage());
				Roma.flow().popup(message);
			}
			e.printStackTrace();
		}
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

}
