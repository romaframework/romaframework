/*
 * Copyright 2010 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.aspect.view.html.taglib;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.html.area.HtmlViewFormArea;
import org.romaframework.aspect.view.html.component.HtmlViewConfigurableEntityForm;
import org.romaframework.aspect.view.html.constants.RequestConstants;

/**
 * this tag represents a form area in the class jsp template.
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public class RomaFormAreaTag extends RomaAbstractTab {
	protected String	name;

	/**
	 * returns the name of the area in the current form
	 * 
	 * @return the name of the area in the current form
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the area in the current form
	 * 
	 * @param name
	 *          the name of the area in the current form
	 */
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int doStartTag() {
		final Object currentForm = pageContext.getRequest().getAttribute(RequestConstants.CURRENT_REQUEST_FORM);
		if (currentForm != null && currentForm instanceof HtmlViewConfigurableEntityForm) {
			try {
				AreaComponent area = ((HtmlViewConfigurableEntityForm) currentForm).getRootArea().searchArea(getName());
				if (area instanceof HtmlViewFormArea) {
					renderArea((HtmlViewFormArea) area);
				}
			} catch (final Exception e) {
				log.error("Error processing area " + getName(), e);
				try {
					pageContext.getOut().print("Error processing area " + getName() + ": " + e);
				} catch (final Exception ex) {
				}
			}
		}
		return SKIP_BODY;
	}

	private void renderArea(final HtmlViewFormArea area) {
		try {
			area.render(pageContext.getOut());
		} catch (final Exception e) {
			log.error("could not process area: " + e, e);
		}
	}
}
