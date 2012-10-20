/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.domain.entity.ComposedEntity;

/**
 * Internal page instance of a ComposedPage. It works weel with composed pages as tabbed pane. Extends ComposedEntityInstance to
 * support inheritance-by-composition pattern.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          Container Class
 * @param <S>
 *          The entity class to be contained and handled as an inheritance.
 */
@ViewClass(label = "")
public abstract class InnerPageComposed<T, S> extends InnerPage<T> implements ComposedEntity<S> {

	public InnerPageComposed(T iContainerPage, S iEntity) {
		super(iContainerPage);
		entity = iEntity;
	}

	public S getEntity() {
		return entity;
	}

	public void setEntity(S iEntity) {
		entity = iEntity;
	}

	@CoreField(embedded = AnnotationConstants.TRUE)
	@ViewField(label = "")
	protected S	entity;

	public boolean canRead() {
		if (getEntity() instanceof Secure)
			return ((Secure) getEntity()).canRead();
		return true;
	}

	public boolean canWrite() {
		if (getEntity() instanceof Secure)
			return ((Secure) getEntity()).canWrite();
		return true;
	}
}
