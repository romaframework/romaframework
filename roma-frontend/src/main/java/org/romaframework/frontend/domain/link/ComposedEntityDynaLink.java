/*
 * Copyright 2008 Luca Garulli (luca.garulli--at--assetdata.it)
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
 */package org.romaframework.frontend.domain.link;

import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.frontend.domain.crud.CRUDHelper;

/**
 * Direct link to a wrapper of an instance.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class ComposedEntityDynaLink extends ObjectDynaLink {
	protected Class<? extends ComposedEntity<?>>	composedClass;

	public ComposedEntityDynaLink(String iTitle, Class<? extends ComposedEntity<?>> iComposedClass, Object iObject, String iPosition) {
		super(iTitle, iObject, iPosition);
		this.composedClass = iComposedClass;
	}

	@Override
	public void onTitle() {
		
		ComposedEntity<?> c = CRUDHelper.getCRUDObject(composedClass, composedClass);
		Roma.aspect(FlowAspect.class).forward(c, position);
	}
}
