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

package org.romaframework.frontend.domain.page;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.security.Secure;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.domain.entity.ComposedEntity;

/**
 * Basic implementation of CallerHandler interface. Allows to track the caller. Useful to build page with "back" action.
 * 
 * Extends Page class supporting the ComposedEntity interface allowing the inheritance-by-composition pattern.
 * 
 * @see Page
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class EntityPage<T> extends Page implements ComposedEntity<T> {
	@ValidationField
	protected T	entity;

	public EntityPage(T iEntity) {
		entity = iEntity;
	}

	@CoreField(embedded = AnnotationConstants.TRUE)
	@ViewField(label = "")
	public T getEntity() {
		return entity;
	}

	public void setEntity(T iEntity) {
		entity = iEntity;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public boolean canRead() {
		if (entity instanceof Secure)
			return ((Secure) entity).canRead();
		return true;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public boolean canWrite() {
		if (entity instanceof Secure)
			return ((Secure) entity).canWrite();
		return true;
	}
}
