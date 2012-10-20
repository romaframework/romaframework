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

package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.page.ContainerPage;

/**
 * Page to handle Create, Modify and View sides of CRUD. In handles internal sub-pages rendered as tabs by default.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @param <T>
 *          Domain Object to handle
 */
public abstract class CRUDTabbedInstance<T> extends CRUDInstance<T> {

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	@ValidationField
	protected ContainerPage<Object>	tabs	= new ContainerPage<Object>();

	public CRUDTabbedInstance() {
	}

	public CRUDTabbedInstance(T iEntity) {
		super(iEntity);
	}

	public ContainerPage<Object> getTabs() {
		return tabs;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	@Override
	public T getEntity() {
		return super.getEntity();
	}
}
