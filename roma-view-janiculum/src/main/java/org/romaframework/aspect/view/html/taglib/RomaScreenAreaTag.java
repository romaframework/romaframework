/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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

import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.screen.HtmlViewScreen;
import org.romaframework.core.Roma;

/**
 * this tag represents a screen area in the screen jsp template.
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public class RomaScreenAreaTag extends RomaAbstractTab {
	protected String	name;

	/**
	 * returns the name of the area of the screen
	 * 
	 * @return the name of the area of the screen
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the area of the screen
	 * 
	 * @param name
	 *          the name of the area of the screen
	 */
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int doStartTag() {
		final ViewAspect view = Roma.aspect(ViewAspect.class);
		final HtmlViewScreen screen = (HtmlViewScreen) view.getScreen();
		final HtmlViewScreenArea area = (HtmlViewScreenArea) screen.getArea(getName());
		if (area == null) {
			return SKIP_BODY;
		}

		renderArea(area);
		return SKIP_BODY;
	}

	private void renderArea(final HtmlViewScreenArea area) {
		try {
			area.render(pageContext.getOut());
		} catch (final Exception e) {
			log.error("could not process area: " + e, e);
		}
	}
}
