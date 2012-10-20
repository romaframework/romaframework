/*
 *
 * Copyright 2009 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.frontend.domain.searchengine;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;

/**
 * @author molino
 * 
 */
public interface QueryItem extends ViewCallback {

	@ViewField(render = ViewConstants.RENDER_BUTTON, label = "")
	public String getText();

	@ViewField(visible = AnnotationConstants.FALSE)
	public void onText();

	@ViewField(visible = AnnotationConstants.FALSE)
	public QueryOperation getOperation();

	@ViewField(position = "form://itemActions", style = "searchEngineAddRightFilter")
	public void addRight();

	@ViewField(position = "form://itemActions", style = "searchEngineAddLeftFilter")
	public void addLeft();

	@ViewField(position = "form://itemActions", style = "searchEngineRemoveFilter")
	public void remove();

	@ViewField(position = "form://itemActions", style = "searchEngineEditFilter")
	public void edit();

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

}
