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
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;

/**
 * Internal page instance of a ComposedPage. It works weel with composed pages as tabbed pane.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          Container class
 */
@ViewClass(label = "")
public abstract class InnerPage<T> {

	@ViewField(visible = AnnotationConstants.FALSE)
	protected T	containerPage;

	public InnerPage(T iContainerPage) {
		containerPage = iContainerPage;
	}

	public T getContainerPage() {
		return containerPage;
	}
}
