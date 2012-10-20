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

import java.util.Date;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.session.SessionAccount;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;

@CoreClass(orderFields = "account lastAccessed source created")
public abstract class ActiveSessionListable {
	@ViewField(visible = AnnotationConstants.FALSE)
	protected SessionInfo	session;

	public ActiveSessionListable(SessionInfo iEntity) {
		session = iEntity;
	}

	public SessionAccount getAccount() {
		return session.getAccount();
	}

	@ViewField(render = ViewConstants.RENDER_DATETIME, format = "dd/MM/yyyy HH:mm:ss")
	public abstract Date getLastAccessed();

	public String getSource() {
		return session.getSource();
	}

	@ViewField(render = ViewConstants.RENDER_DATETIME, format = "dd/MM/yyyy HH:mm:ss")
	public Date getCreated() {
		return session.getCreated();
	}

	public SessionInfo getSession() {
		return session;
	}
}
