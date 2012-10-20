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

package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewAction;

/**
 * Listener for page changing on CRUD.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface PagingListener {
	/**
	 * Load a page.
	 * 
	 * @param iFrom
	 *          the start index of page.
	 * @param iTo
	 *          the destination index.
	 * 
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void loadPage(int iFrom, int iTo);

	/**
	 * Load All elements without paging.
	 */
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void loadAllPages();
}
