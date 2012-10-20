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

package org.romaframework.module.users.domain;

import java.io.Serializable;
import java.util.Date;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;

public class Tracking implements Serializable {

	private static final long	serialVersionUID	= 2430755208131177375L;

	@ViewField(enabled = AnnotationConstants.FALSE)
	private Date							when;

	@ViewField(enabled = AnnotationConstants.FALSE)
	private BaseAccount				account;

	@ViewField(enabled = AnnotationConstants.FALSE)
	private String						from;

	@ViewField(enabled = AnnotationConstants.FALSE)
	private String						userAgent;

	@ViewField(render = ViewConstants.RENDER_TEXTAREA)
	private String						notes;

	public Tracking() {
	}

	public Tracking(String notes) {
		when = new Date();
		SessionInfo sess = Roma.session().getActiveSessionInfo();
		if (sess != null) {
			account = (BaseAccount) sess.getAccount();
			from = sess.getSource();
			userAgent = sess.getUserAgent();
		}
		this.notes = notes;
	}

	public BaseAccount getAccount() {
		return account;
	}

	public void setAccount(BaseAccount user) {
		account = user;
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

}
