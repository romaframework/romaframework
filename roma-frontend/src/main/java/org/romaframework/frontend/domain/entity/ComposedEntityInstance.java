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

package org.romaframework.frontend.domain.entity;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreClass.LOADING_MODE;
import org.romaframework.aspect.security.Secure;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.core.domain.entity.ComposedEntity;

/**
 * Basic implementation of the inheritance-by-composition pattern. It delegates the Secure checks, toString(), hashCode() and
 * equals() to the underlying entity.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          The entity class to be contained and handled as an inheritance.
 */
@CoreClass(loading = LOADING_MODE.EARLY)
public class ComposedEntityInstance<T> implements ComposedEntity<T> {

	protected T	entity;

	public ComposedEntityInstance() {
	}

	public ComposedEntityInstance(T iEntity) {
		entity = iEntity;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T iEntity) {
		entity = iEntity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().equals(getClass()))
			return false;

		ComposedEntityInstance<T> other = (ComposedEntityInstance<T>) obj;
		if (entity == null && other.entity == null)
			return super.equals(obj);

		return entity.equals(other.entity);
	}

	@Override
	public int hashCode() {
		return entity != null ? entity.hashCode() : super.hashCode();
	}

	@Override
	public String toString() {
		return entity != null ? entity.toString() : super.toString();
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
