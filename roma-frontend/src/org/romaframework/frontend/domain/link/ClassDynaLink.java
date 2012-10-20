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
 */
package org.romaframework.frontend.domain.link;

import org.romaframework.core.Roma;

/**
 * Link to a cached class instance.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class ClassDynaLink extends AbstractTitleDynaLink {
	protected Class<?>	clazz;

	public ClassDynaLink(final String iTitle, final Class<?> iClass) {
		super(iTitle);
		this.clazz = iClass;
	}

	public ClassDynaLink(final String iTitle, final Class<?> iClass, final String iPosition) {
		super(iTitle, iPosition);
		this.clazz = iClass;
	}

	public void onTitleClick() {
		Roma.flow().forward(clazz, position);
	}
}
