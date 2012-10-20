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

/**
 * Dynamic link object to use when you want to create dynamic links. The link title is stored inside the object.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class AbstractTitleDynaLink extends AbstractDynaLink {
	protected String	title;
	protected String	position	= "body";

	public AbstractTitleDynaLink(final String iTitle, final String iPosition) {
		this(iTitle);
		position = iPosition;
	}

	public AbstractTitleDynaLink(final String iTitle) {
		title = iTitle;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
